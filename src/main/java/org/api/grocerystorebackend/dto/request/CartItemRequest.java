package org.api.grocerystorebackend.dto.request;

import lombok.Data;
import org.api.grocerystorebackend.dto.response.CartItemDTO;

@Data
public class CartItemRequest {
    private Long userID;
    private CartItemDTO cartItemDTO;
}
