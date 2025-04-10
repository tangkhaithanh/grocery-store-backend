package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;
    private String imageUrl;
    private String userFullName;
    private Long userId;
    private Long orderId;
    private LocalDateTime createdAt;
}
