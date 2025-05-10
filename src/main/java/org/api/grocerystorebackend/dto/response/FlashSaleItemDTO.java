package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashSaleItemDTO {
    private Long id;
    private BigDecimal flashSalePrice;
    private Integer stockQuantity;
    private Integer soldQuantity;
    private Integer maxPerCustomer;

    private ProductDTO product;
}
