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
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discount;
    private int quantity;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String categoryName;
    private String categoryImage;
    private List<String> imageUrls;
}
