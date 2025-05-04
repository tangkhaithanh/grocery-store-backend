package org.api.grocerystorebackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.api.grocerystorebackend.dto.request.ReviewRequest;
import org.api.grocerystorebackend.dto.response.DeliveredOrderDTO;
import org.api.grocerystorebackend.dto.response.ProductReviewStatusDTO;
import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.dto.response.ReviewStatsDTO;
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
import java.util.*;
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
        // 1. Lấy orderItem
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("Order item not found"));

        // 2. Tạo Review
        Review review = new Review();
        review.setOrderItem(orderItem);
        review.setUser(user);
        review.setRating(request.getRating());

        // 3. Comment (có thể null / rỗng)
        String comment = request.getComment();
        review.setComment((comment == null || comment.isBlank()) ? null : comment.trim());

        // 4. Danh sách hình ảnh
        List<String> imageUrls = request.getImageUrls();          // ← List<String>
        if (imageUrls != null && !imageUrls.isEmpty()) {
            List<ReviewImage> images = imageUrls.stream()
                    .filter(url -> url != null && !url.isBlank())
                    .map(url -> {
                        ReviewImage img = new ReviewImage();
                        img.setReview(review);                    // gắn khóa ngoại
                        img.setImageUrl(url.trim());
                        return img;
                    })
                    .toList();
            review.setImages(images);
        }

        // 5. Thời gian tạo
        review.setCreatedAt(LocalDateTime.now());

        // 6. Lưu (cascade ALL sẽ tự lưu ReviewImage)
        reviewRepository.save(review);
    }

    @Override
    public Page<ReviewDTO> getReviewsByProduct(Long productId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByOrderItemProductId(productId, pageable);
        return reviews.map(reviewMapper::toDTO);
    }

    @Override
    public ReviewStatsDTO getProductReviewStats(Long productId) {
        // Get all reviews for the product
        List<Review> reviews = reviewRepository.findAllByOrderItemProductId(productId);

        // If there are no reviews, return empty stats
        if (reviews.isEmpty()) {
            Map<Integer, Long> emptyDistribution = new HashMap<>();
            for (int i = 1; i <= 5; i++) {
                emptyDistribution.put(i, 0L);
            }
            return new ReviewStatsDTO(0.0, 0L, emptyDistribution);
        }

        // Calculate average rating
        double averageRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // Count total reviews
        long totalReviews = reviews.size();

        // Calculate distribution
        Map<Integer, Long> distribution = reviews.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        // Ensure all ratings 1-5 are represented in the map
        for (int i = 1; i <= 5; i++) {
            distribution.putIfAbsent(i, 0L);
        }

        return new ReviewStatsDTO(averageRating, totalReviews, distribution);
    }
}