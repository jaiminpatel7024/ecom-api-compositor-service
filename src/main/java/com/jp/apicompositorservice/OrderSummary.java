package com.jp.apicompositorservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSummary {

    private String orderId;
    private String fullname;
    private String productName;
    private String productBrand;
    private Integer quantities;
    private Long orderTotal;
    private String paymentStatus;

    @Override
    public String toString() {
        return "OrderSummary{" +
                "orderId='" + orderId + '\'' +
                ", fullname='" + fullname + '\'' +
                ", productName='" + productName + '\'' +
                ", productBrand='" + productBrand + '\'' +
                ", quantities=" + quantities +
                ", orderTotal=" + orderTotal +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
