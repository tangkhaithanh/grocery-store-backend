package org.api.grocerystorebackend.dto.request;

import lombok.Data;

@Data
public class CancelOrderRequest {
    private Long userID;
    private Long orderID;
}
