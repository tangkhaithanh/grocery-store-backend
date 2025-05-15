package org.api.grocerystorebackend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {
    private Long productId;
    private Long flashSaleItemId; // nullable
    private Integer quantity;
    private BigDecimal price;
}
