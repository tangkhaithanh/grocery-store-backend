package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewStatsDTO {
    private Double averageRating;
    private Long totalReviews;
    private Map<Integer, Long> ratingDistribution; // Map of rating (1-5) to count
}
