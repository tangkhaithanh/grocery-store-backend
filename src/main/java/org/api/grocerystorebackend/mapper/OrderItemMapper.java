package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.entity.OrderItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OrderItemMapper {
    @Autowired
    private ProductMapper productMapper;

    public OrderItemDTO toDTO(OrderItem orderItem, LocalDateTime deliveryAt) {
        boolean canReview = false;

        if (orderItem.getReview() == null && deliveryAt != null) {
            LocalDateTime now = LocalDateTime.now();
            canReview = now.isBefore(deliveryAt.plusMonths(1));
        }

        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                productMapper.toDTO(orderItem.getProduct()),
                canReview
        );
    }

}
