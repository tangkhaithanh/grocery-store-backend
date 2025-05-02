package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.request.ReviewRequest;
import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IReviewService {
    void createReview(Long orderItemId, User user, ReviewRequest request);
    Page<ReviewDTO> getReviewsByProduct(Long productId, Pageable pageable);
}
