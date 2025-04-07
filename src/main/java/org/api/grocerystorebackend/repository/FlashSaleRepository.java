package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.entity.FlashSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlashSaleRepository extends JpaRepository<FlashSale, Long> {

}
