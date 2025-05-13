package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.Cart;
import org.api.grocerystorebackend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
