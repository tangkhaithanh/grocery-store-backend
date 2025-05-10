package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.FlashSaleDTO;
import org.api.grocerystorebackend.dto.response.FlashSaleItemDTO;
import org.api.grocerystorebackend.entity.FlashSale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FlashSaleMapper {
    @Autowired
    FlashSaleItemMapper flashSaleItemMapper;

    public FlashSaleDTO toDTO(FlashSale flashSale) {
        List<FlashSaleItemDTO> flashSaleItemDTOS = flashSale.getFlashSaleItems().stream()
                .map(flashSaleItem -> {
                    System.out.println(flashSaleItem.getId());
                    return flashSaleItemMapper.toFlashSaleItemDTO(flashSaleItem);

                })
                .toList();
        FlashSaleDTO dto = new FlashSaleDTO();
        dto.setId(flashSale.getId());
        dto.setName(flashSale.getName());
        dto.setDescription(flashSale.getDescription());
        dto.setStartTime(flashSale.getStartTime());
        dto.setEndTime(flashSale.getEndTime());

        dto.setFlashSaleItems(flashSaleItemDTOS);

        return dto;
    }
}
