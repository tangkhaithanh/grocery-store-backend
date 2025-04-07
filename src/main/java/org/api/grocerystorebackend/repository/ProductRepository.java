package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Add Filter method:
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("""
        SELECT p FROM Product p
        LEFT JOIN p.reviews r
        GROUP BY p
        ORDER BY AVG(r.rating) DESC
    """)
    Page<Product> findAllOrderByAverageRatingDesc(Pageable pageable);

        @Query("""
        SELECT p FROM Product p
        JOIN p.orderItems oi
        JOIN oi.order o
        WHERE o.status = 'COMPLETED'
          AND o.createdAt >= :startDate
        GROUP BY p
        ORDER BY SUM(oi.quantity) DESC
""")
    Page<Product> findBestSellingLast7Days(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    // Chức năng search:
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

}
