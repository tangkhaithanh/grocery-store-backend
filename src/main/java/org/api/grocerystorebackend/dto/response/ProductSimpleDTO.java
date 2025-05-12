package org.api.grocerystorebackend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSimpleDTO {
    private Long id;
    private String name;
    private BigDecimal price;
    private String imageUrl;
}
