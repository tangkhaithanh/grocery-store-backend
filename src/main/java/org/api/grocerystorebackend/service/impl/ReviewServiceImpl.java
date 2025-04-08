package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.request.ReviewRequest;
import org.api.grocerystorebackend.dto.response.DeliveredOrderDTO;
import org.api.grocerystorebackend.dto.response.ProductReviewStatusDTO;
import org.api.grocerystorebackend.dto.response.ReviewDTO;
import org.api.grocerystorebackend.entity.*;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.mapper.ReviewMapper;
import org.api.grocerystorebackend.repository.OrderRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.repository.ReviewRepository;
import org.api.grocerystorebackend.service.IReviewService;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductRepository productRepository;

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private OrderRepository orderRepository;


    @Override
    public void createReview(Long productId,Long orderId, User user, ReviewRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));

        Review review = new Review();
        review.setOrder(order);
        review.setProduct(product);
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
    public List<ReviewDTO> getReviewsByProduct(Long productId, User currentUser) {
        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        return reviews.stream()
                .map(review -> reviewMapper.toDTO(review))
                .toList();
    }

    @Override
    public List<DeliveredOrderDTO> getDeliveredOrdersWithReviewStatus(User user) {
        List<Order> orders = orderRepository.findByUserIdAndStatus(user.getId(), StatusOrderType.DELIVERED);

        return orders.stream().map(order -> {
            List<Review> reviews = reviewRepository.findByUserIdAndOrderId(user.getId(), order.getId());
            Set<Long> reviewedProductIds = reviews.stream()
                    .map(r -> r.getProduct().getId())
                    .collect(Collectors.toSet());

            List<ProductReviewStatusDTO> products = order.getOrderItems().stream()
                    .map(item -> {
                        Product product = item.getProduct();
                        String thumbnail = product.getImages().stream()
                                .findFirst()
                                .map(ProductImage::getImageUrl)
                                .orElse(null);

                        return new ProductReviewStatusDTO(
                                product.getId(),
                                product.getName(),
                                thumbnail,
                                reviewedProductIds.contains(product.getId())
                        );
                    }).toList();

            return new DeliveredOrderDTO(
                    order.getId(),
                    order.getDeliveryAt(),
                    products
            );
        }).toList();
    }
}