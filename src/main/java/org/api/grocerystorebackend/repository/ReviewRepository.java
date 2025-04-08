package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.entity.Review;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);

    List<Review> findByUserIdAndOrderId(Long userId, Long orderId);
}
