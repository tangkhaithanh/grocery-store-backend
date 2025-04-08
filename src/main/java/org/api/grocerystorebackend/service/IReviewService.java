package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.request.ReviewRequest;
import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.entity.User;

import java.util.List;

public interface IReviewService {
    void createReview(Long productId,Long orderId, User user, ReviewRequest request);
    List<ReviewDTO> getReviewsByProduct(Long productId);
}
