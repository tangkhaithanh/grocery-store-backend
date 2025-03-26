package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.FavouriteProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteProductRepository extends JpaRepository<FavouriteProduct, Long> {
}
