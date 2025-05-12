package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.FavouriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Long> {
    List<FavouriteProduct> findByUserId(Long userId);

    Optional<FavouriteProduct> findByUserIdAndProductId(Long userId, Long productId);
}
