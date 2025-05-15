package org.api.grocerystorebackend.repository;

import jakarta.transaction.Transactional;
import org.api.grocerystorebackend.entity.Cart;
import org.api.grocerystorebackend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface CartItemRepository  extends JpaRepository<CartItem, Long> {
    @Query("SELECT ct FROM CartItem ct WHERE ct.product.id =:id AND ct.flashSaleItem IS NULL AND ct.cart.id=:cartID")
    Optional<CartItem> findByProductIdWithNotFL(@Param("id") Long id,  @Param("cartID") Long cartID);

    @Query("SELECT ct FROM CartItem ct WHERE ct.product.id =:id AND ct.flashSaleItem IS NOT NULL AND ct.cart.id=:cartID")
    Optional<CartItem> findByProductIdWithFL(@Param("id") Long id, @Param("cartID") Long cartID);

    Long cart(Cart cart);

    @Modifying(clearAutomatically = true)
    @Transactional
    @Query("DELETE FROM CartItem c WHERE c.cart.user.id = :userId AND c.product.id = :productId AND " +
            "((c.flashSaleItem IS NULL AND :flashSaleItemId IS NULL) OR " +
            "(c.flashSaleItem IS NOT NULL AND c.flashSaleItem.id = :flashSaleItemId))")
    void deleteByUserIdAndProductIdAndFlashSaleItemId(@Param("userId") Long userId,
                                                      @Param("productId") Long productId,
                                                      @Param("flashSaleItemId") Long flashSaleItemId);


    @Query("SELECT ct FROM CartItem ct WHERE ct.product.id = :productId " +
            "AND ct.flashSaleItem.id = :flashSaleItemId AND ct.cart.id = :cartId")
    Optional<CartItem> findByProductIdAndFlashSaleItemId(@Param("productId") Long productId,
                                                         @Param("flashSaleItemId") Long flashSaleItemId,
                                                         @Param("cartId") Long cartId);

}
