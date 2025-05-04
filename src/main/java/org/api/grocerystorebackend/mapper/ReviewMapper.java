package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.entity.Review;
import org.api.grocerystorebackend.entity.ReviewImage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
    public class ReviewMapper {
        public ReviewDTO toDTO(Review review) {
            if (review == null) return null;

            List<String> imageUrls = review.getImages() == null
                    ? List.of()
                    : review.getImages()
                    .stream()
                    .map(ReviewImage::getImageUrl)
                    .collect(Collectors.toList());

            return new ReviewDTO(
                    review.getId(),
                    review.getRating(),
                    review.getComment(),
                    imageUrls,                       // <-- danh sách ảnh
                    review.getUser().getFullName(),
                    review.getUser().getId(),
                    review.getOrderItem().getId(),
                    review.getCreatedAt()
            );
        }
    }
