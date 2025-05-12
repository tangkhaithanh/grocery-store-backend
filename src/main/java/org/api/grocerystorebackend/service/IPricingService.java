package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.entity.Product;

import java.math.BigDecimal;
import java.util.Optional;

public interface IPricingService {
    BigDecimal getEffectivePrice(Product product);
    Optional<FlashSaleItem> getFlashSaleItem(Product product);
}
