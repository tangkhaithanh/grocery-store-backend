package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {
    @Autowired
    private OrderItemMapper orderItemMapper;

    public OrderDTO toDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems()
                .stream()
                .map(orderItemMapper::toDTO)
                .toList();

        return new OrderDTO(
                order.getId(),
                order.getTotalAmount(),
                order.getDeliveryAt(),
                order.getStatus(),
                orderItemDTOs
        );
    }
}
