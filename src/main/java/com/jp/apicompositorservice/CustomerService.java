package com.jp.apicompositorservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class CustomerService
{
    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);


    @Autowired
    @Qualifier("customer-service-web-client")
    WebClient webClient;

    public boolean validateToken(String token) {

        log.info("Validating token within the CustomerService: {}", token);
        log.info("Sending request to customer service to validate token: {}", token);

        try {
            String response = webClient.get()
                    .uri("/validate")
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(status -> status.value() == 401, responseTemp ->
                            Mono.error(new WebClientResponseException("Unauthorized", 401, "Unauthorized", null, null, null)))
                    .bodyToMono(String.class)
                    .block();

            log.info("Response from customer service: {}", response);
            return response.equalsIgnoreCase("valid");

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                log.error("Unauthorized request.");
                return false;
            }
            throw e;
        }

    }

    public User getUserData(String token,String username) {

        log.info("Sending request to customer service to get user data for username {} with  token: {}", username, token);

        try {
            User response = webClient.get()
                    .uri("/users/view/"+username)
                    .header("Authorization", token)
                    .retrieve()
                    .onStatus(status -> status.value() == 401, responseTemp ->
                            Mono.error(new WebClientResponseException("Unauthorized", 401, "Unauthorized", null, null, null)))
                    .bodyToMono(User.class)
                    .block(); // Blocking call, ensure you use it only in non-reactive flows

            log.info("Response from auth service: {}", response);
            return response;

        } catch (WebClientResponseException e) {
            if (e.getStatusCode().value() == 401) {
                log.error("Unauthorized request.");
                return null; // Or throw a custom exception
            }
            throw e; // Re-throw other exceptions
        }

    }
}
