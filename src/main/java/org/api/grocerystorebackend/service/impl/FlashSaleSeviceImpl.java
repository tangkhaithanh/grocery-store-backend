package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.repository.FlashSaleItemRepository;
import org.api.grocerystorebackend.service.IFlashSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FlashSaleSeviceImpl implements IFlashSaleService {
    @Autowired
    FlashSaleItemRepository flashSaleItemRepository;
    @Override
    public Optional<FlashSaleItem> findProductInFlashSale(Long productId) {
        Optional<FlashSaleItem> flashSaleItem = flashSaleItemRepository.findProductInFL(productId, LocalDateTime.now());
        return flashSaleItem;
    }
}
