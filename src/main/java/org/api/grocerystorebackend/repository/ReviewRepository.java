package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.entity.Review;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    Page<Review> findByOrderItemProductId(Long productId, Pageable pageable);

    // New method for getting all reviews for a product (no pagination)
    List<Review> findAllByOrderItemProductId(Long productId);
}
