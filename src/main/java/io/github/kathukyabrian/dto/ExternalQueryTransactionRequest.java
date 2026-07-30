package io.github.kathukyabrian.dto;

import lombok.Data;

@Data
public class ExternalQueryTransactionRequest {
    private String originatorConversationId;
    private String shortCode;
    private String passkey;
    private String consumerSecret;
    private String consumerKey;
    private String passKey;
}
