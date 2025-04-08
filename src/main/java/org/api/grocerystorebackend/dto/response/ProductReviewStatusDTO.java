package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewStatusDTO {
    private Long productId;
    private String productName;
    private String image;
    private boolean reviewed;
}
