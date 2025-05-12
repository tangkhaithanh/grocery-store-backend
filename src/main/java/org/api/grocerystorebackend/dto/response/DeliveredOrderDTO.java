package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveredOrderDTO {
    private Long orderId;
    private LocalDateTime deliveredAt;
    private List<ProductReviewStatusDTO> products;
}
