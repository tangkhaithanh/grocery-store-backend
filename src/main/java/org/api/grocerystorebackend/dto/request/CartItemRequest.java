package org.api.grocerystorebackend.dto.request;

import lombok.Data;
import org.api.grocerystorebackend.dto.response.CartItemDTO;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.dto.response.ProductSimpleDTO;

import java.math.BigDecimal;

@Data
public class CartItemRequest {
    private Long flashSaleId;
    private int quantity;
    private BigDecimal price;
    private ProductSimpleDTO product;
}
