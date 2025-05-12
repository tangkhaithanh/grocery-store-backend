package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Add Filter method:
    Page<Product> findAllByOrderByPriceAsc(Pageable pageable);
    Page<Product> findAllByOrderByPriceDesc(Pageable pageable);
    Page<Product> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("""
    SELECT p FROM Product p
    LEFT JOIN p.orderItems oi
    LEFT JOIN oi.review r
    WHERE r IS NOT NULL
    GROUP BY p
    ORDER BY AVG(r.rating) DESC
""")
    Page<Product> findAllOrderByAverageRatingDesc(Pageable pageable);



    @Query("""
        SELECT p FROM Product p
        JOIN p.orderItems oi
        JOIN oi.order o
        WHERE o.status = 'DELIVERED'
          AND o.createdAt >= :startDate
        GROUP BY p
        ORDER BY SUM(oi.quantity) DESC
""")
    Page<Product> findBestSellingLast7Days(@Param("startDate") LocalDateTime startDate, Pageable pageable);
    // Chức năng search:

    @Query("""
        SELECT p FROM Product p
        WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%'))
           OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :kw, '%'))
        ORDER BY CASE
                   WHEN LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')) THEN 0
                   ELSE 1
                 END
    """)
    Page<Product> searchByNameOrCategory(@Param("kw") String keyword, Pageable pageable);

    @Query("""
    SELECT p FROM Product p
    WHERE SIZE(p.favouriteProducts) > 0
      AND (p.quantity - COALESCE((
            SELECT SUM(oi.quantity) FROM OrderItem oi WHERE oi.product = p
      ), 0)) > 1
      AND (
            SELECT AVG(r.rating)
            FROM Review r
            WHERE r.orderItem.product = p
      ) > 1.0
    """)
    List<Product> findFeatureProductsForFlashSale(Pageable pageable);


}
