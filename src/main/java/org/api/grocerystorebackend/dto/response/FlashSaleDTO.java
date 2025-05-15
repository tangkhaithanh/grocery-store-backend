package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.grocerystorebackend.entity.FlashSaleItem;
import org.api.grocerystorebackend.enums.FlashSaleStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashSaleDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime endTime;
    private LocalDateTime startTime;
    private FlashSaleStatus status;
    private List<FlashSaleItemDTO> flashSaleItems;
}
