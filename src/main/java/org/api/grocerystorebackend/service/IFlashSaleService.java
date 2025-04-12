package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.entity.FlashSaleItem;

import java.util.Optional;

public interface IFlashSaleService {
    Optional<FlashSaleItem> findProductInFlashSale(Long productId);
    
}
