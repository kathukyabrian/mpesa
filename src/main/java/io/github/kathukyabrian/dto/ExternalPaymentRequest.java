package io.github.kathukyabrian.dto;

import lombok.Data;

@Data
public class ExternalPaymentRequest {
    private String consumerKey;
    private String consumerSecret;
    private String passKey;
    private String shortCode;
    private Integer amount;
    private String phoneNumber;
    private String accountRef;
    private String transactionDesc;
    private String transactionType;
}
