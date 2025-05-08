package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.FlashSaleDTO;
import org.api.grocerystorebackend.entity.FlashSale;
import org.api.grocerystorebackend.entity.FlashSaleItem;

import java.util.List;
import java.util.Optional;

public interface IFlashSaleService {
    Optional<FlashSaleItem> findProductInFlashSale(Long productId);

    void activeFlashSales();
    void expireFlashSales();

    FlashSaleDTO createProductInFlashSale();

    List<FlashSaleDTO> getFlashSaleList();
}
