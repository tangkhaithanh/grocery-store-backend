package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FlashSaleItemRepository extends JpaRepository<FlashSaleItem, Long> {
    @Query("""
        SELECT f FROM FlashSaleItem f 
        WHERE f.product.id = :productId 
        AND f.flashSale.startTime <= :now 
        AND f.flashSale.endTime >= :now
    """)
    Optional<FlashSaleItem> findActiveFlashSaleItem(@Param("productId") Long productId, @Param("now") LocalDateTime now);

    @Query("""
        SELECT f FROM FlashSaleItem f 
        WHERE f.product.id = :productId 
        AND f.flashSale.startTime <= :now 
        AND f.flashSale.endTime >= :now
        AND f.flashSale.status = 'ACTIVE'
""")
    Optional<FlashSaleItem> findProductInFL(@Param("productId") Long productId, @Param("now") LocalDateTime now);
}
