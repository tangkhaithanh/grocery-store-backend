package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private int rating;
    private String comment;

    /** danh sách URL hình thay cho 1 imageUrl */
    private List<String> imageUrls;

    private String userFullName;
    private Long userId;
    private Long orderItemId;
    private LocalDateTime createdAt;
}
