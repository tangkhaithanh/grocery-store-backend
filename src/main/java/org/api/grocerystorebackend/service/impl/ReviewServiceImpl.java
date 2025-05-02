package org.api.grocerystorebackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.api.grocerystorebackend.dto.request.ReviewRequest;
import org.api.grocerystorebackend.dto.response.DeliveredOrderDTO;
import org.api.grocerystorebackend.dto.response.ProductReviewStatusDTO;
import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.entity.*;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.mapper.ReviewMapper;
import org.api.grocerystorebackend.repository.OrderItemRepository;
import org.api.grocerystorebackend.repository.OrderRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.repository.ReviewRepository;
import org.api.grocerystorebackend.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements IReviewService {
    @Autowired
    private ReviewRepository reviewRepository;


    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private OrderItemRepository orderItemRepository;


    @Override
    public void createReview(Long orderItemId, User user, ReviewRequest request) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));
        Review review = new Review();
        review.setOrderItem(orderItem);
        review.setUser(user);
        review.setRating(request.getRating());
        String comment = request.getComment();
        review.setComment((comment == null || comment.trim().isEmpty()) ? null : comment.trim());
        String imageUrl = request.getImageUrl();
        review.setImageUrl((imageUrl == null || imageUrl.trim().isEmpty()) ? null : imageUrl.trim());
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    public Page<ReviewDTO> getReviewsByProduct(Long productId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByOrderItemProductId(productId, pageable);
        return reviews.map(reviewMapper::toDTO);
    }


}