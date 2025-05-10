package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.grocerystorebackend.entity.FlashSaleItem;

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
    private List<FlashSaleItemDTO> flashSaleItems;
}
