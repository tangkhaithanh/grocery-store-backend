package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.entity.Review;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
    public class ReviewMapper {
        public ReviewDTO toDTO(Review review) {
            return new ReviewDTO(
                    review.getId(),
                    review.getRating(),
                    review.getComment(),
                    review.getImageUrl(),
                    review.getUser().getFullName(),
                    review.getUser().getId(),
                    review.getOrder().getId(),
                    review.getCreatedAt()
            );
        }
    }
