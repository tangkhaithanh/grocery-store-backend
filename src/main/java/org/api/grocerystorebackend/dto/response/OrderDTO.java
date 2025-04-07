package org.api.grocerystorebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.api.grocerystorebackend.enums.StatusOrderType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private BigDecimal totalAmount;
    private LocalDateTime deliveryAt;
    private StatusOrderType status;
    private List<OrderItemDTO> orderItems;
}
