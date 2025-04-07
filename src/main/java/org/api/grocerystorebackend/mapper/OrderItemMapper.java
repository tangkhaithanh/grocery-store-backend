package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    @Autowired
    private ProductMapper productMapper;

    public OrderItemDTO toDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                productMapper.toDTO(orderItem.getProduct())
        );
    }
}
