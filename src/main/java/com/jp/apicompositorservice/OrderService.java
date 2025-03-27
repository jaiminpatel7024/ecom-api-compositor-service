package com.jp.apicompositorservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    @Qualifier("order-processing-service-web-client")
    WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    public List<Order> getOrderList(String token){

        List<Order> orderList = new ArrayList<>();
        log.info("Sending request to order processing service to fetch order list with  token: {}", token);
        /*String response = webClient.get().uri("/validate")
                .header("Authorization", token)
                .retrieve()
                .bodyToMono(String.class).block(); // Current Thread will pause till the final response comes back*/

        try {
            orderList = webClient.get()
                    .uri("/orders/viewAll")
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(status -> status.value() == 401, responseTemp ->
                            Mono.error(new WebClientResponseException("Unauthorized", 401, "Unauthorized", null, null, null)))
                    .bodyToFlux(Order.class)
                    .collectList().block();

            log.info("Received Orderlist with count : {}", orderList.size());
            return orderList;

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
               log.error("Unauthorized request while fetching the order details.");
                return null; // Or throw a custom exception
            }
            throw e; // Re-throw other exceptions
        }

    }
}
