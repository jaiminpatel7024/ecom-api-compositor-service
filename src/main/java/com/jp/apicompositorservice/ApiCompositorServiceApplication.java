package com.jp.apicompositorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ApiCompositorServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(ApiCompositorServiceApplication.class, args);

    }

}
