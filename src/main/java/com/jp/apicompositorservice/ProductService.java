package com.jp.apicompositorservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    @Qualifier("product-catalog-service-web-client")
    WebClient webClient;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public Product getProductData(String token, Long productId){
        log.info("Sending request to product catalog service to product data with product id : {} with  token: {}", productId, token);

        try {
           Product productObj = webClient.get()
                    .uri("/products/view/"+productId)
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(status -> status.value() == 401, responseTemp ->
                            Mono.error(new WebClientResponseException("Unauthorized", 401, "Unauthorized", null, null, null)))
                    .bodyToMono(Product.class)
                    .block();

            log.info("Received Product data : {}", productObj);
            return productObj;

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
               log.error("Unauthorized request while fetching the order details.");
                return null; // Or throw a custom exception
            }
            throw e; // Re-throw other exceptions
        }

    }
}
