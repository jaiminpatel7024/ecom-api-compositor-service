package com.jp.apicompositorservice;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter

public class Order {

    private String orderId;
    private Long productId;
    private Integer quantities;
    private Long orderTotal;
    private String username;
    private LocalDateTime orderTime;
    private String status;
    private String paymentStatus;

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", productId='" + productId + '\'' +
                ", quantities=" + quantities +
                ", orderTotal=" + orderTotal +
                ", username='" + username + '\'' +
                ", orderTime=" + orderTime +
                ", status='" + status + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
