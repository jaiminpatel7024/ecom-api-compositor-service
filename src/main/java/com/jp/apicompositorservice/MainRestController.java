package com.jp.apicompositorservice;

import com.netflix.discovery.converters.Auto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class MainRestController {

    @Autowired
    CustomerService customerService;

    @Autowired
    OrderService orderService;

    @Autowired
    ProductService productService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final Logger log = LoggerFactory.getLogger(MainRestController.class);

    @GetMapping("/test")
    public ResponseEntity<?> testApiCompositorService() {
        return ResponseEntity.ok("API Compositor Service running fine");
    }


    @GetMapping("/orders/summary")
    public ResponseEntity<?> getOrdersSummary(@RequestHeader("Authorization") String token){

        //Authenticate
        log.info("Received request to view all orders");

            if(customerService.validateToken(token)){

                List<OrderSummary> cachedSummary = (List<OrderSummary>)redisTemplate.opsForValue().
                        get("OrderSummary");

                if(cachedSummary!=null && cachedSummary.size()>0){
                    log.info("Replying the data from the cache ");
                    return ResponseEntity.ok(cachedSummary);
                }

                List<OrderSummary> summaryData = new ArrayList<>();

                //Get List of all orders
                List<Order> orderList = orderService.getOrderList(token);

                Map<String,String> userData = new HashMap<>();
                Map<Long,Product> productData = new HashMap<>();

                orderList.stream().forEach(order -> {
                    OrderSummary summaryObj = new OrderSummary();
                    summaryObj.setOrderId(order.getOrderId());
                    summaryObj.setOrderTotal(order.getOrderTotal());
                    summaryObj.setQuantities(order.getQuantities());
                    summaryObj.setPaymentStatus(order.getPaymentStatus());


                    //Get User Details
                    if(userData.get(order.getUsername())!=null){
                        summaryObj.setFullname(userData.get(order.getUsername()));
                    }else {
                        User userObj = customerService.getUserData(token,order.getUsername());
                        if(userObj!=null){
                            summaryObj.setFullname(userObj.getFullname());
                            userData.put(order.getUsername(),userObj.getFullname());
                        }
                    }

                    //Get product details
                    if(productData.get(order.getProductId())!=null){
                        summaryObj.setProductName(productData.get(order.getProductId()).getName());
                        summaryObj.setProductBrand(productData.get(order.getProductId()).getBrand());
                    }else{
                        Product productObj = productService.getProductData(token,order.getProductId());
                        if(productObj!=null){
                            summaryObj.setProductName(productObj.getName());
                            summaryObj.setProductBrand(productObj.getBrand());

                            productData.put(order.getProductId(),productObj);
                        }
                    }

                    summaryData.add(summaryObj);
                });

                redisTemplate.opsForValue().set("OrderSummary",summaryData);

                return ResponseEntity.ok(summaryData);

            } else {
                return ResponseEntity.status(401).body("Unauthorized");
            }
    }
}
