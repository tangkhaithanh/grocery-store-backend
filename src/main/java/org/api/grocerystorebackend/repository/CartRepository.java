package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.dto.response.CartDTO;
import org.api.grocerystorebackend.entity.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.cart.user.id = :userId AND c.product.id = :productId AND " +
            "(:flashSaleItemId IS NULL AND c.flashSaleItem IS NULL OR c.flashSaleItem.id = :flashSaleItemId)")
    void deleteByUserIdAndProductIdAndFlashSaleItemId(@Param("userId") Long userId,
                                                      @Param("productId") Long productId,
                                                      @Param("flashSaleItemId") Long flashSaleItemId);

}
