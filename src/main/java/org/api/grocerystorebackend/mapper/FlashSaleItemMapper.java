package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.FlashSaleItemDTO;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FlashSaleItemMapper {

    @Autowired
    private ProductMapper productMapper;

    public FlashSaleItemDTO toFlashSaleItemDTO(FlashSaleItem item) {
        ProductDTO productDTO =productMapper.toDTO(item.getProduct());
        FlashSaleItemDTO dto = new FlashSaleItemDTO();
        dto.setId(item.getId());
        dto.setFlashSalePrice(item.getFlashSalePrice());
        dto.setStockQuantity(item.getStockQuantity());
        dto.setSoldQuantity(item.getSoldQuantity());
        dto.setMaxPerCustomer(item.getMaxPerCustomer());
        dto.setProduct(productDTO);

        return dto;
    }
}
