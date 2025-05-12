package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.entity.FlashSale;
import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.repository.FlashSaleItemRepository;
import org.api.grocerystorebackend.repository.FlashSaleRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.service.IPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
@Service
public class PricingService implements IPricingService {
    @Autowired
    private FlashSaleItemRepository flashSaleItemRepository;

    @Override
    public BigDecimal getEffectivePrice(Product product) {
        return getFlashSaleItem(product)
                .map(FlashSaleItem::getFlashSalePrice)
                .orElse(product.getPrice());
    }

    @Override
    public Optional<FlashSaleItem> getFlashSaleItem(Product product) {
        return flashSaleItemRepository.findActiveFlashSaleItem(product.getId(), LocalDateTime.now());
    }
}
