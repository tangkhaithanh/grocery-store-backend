package org.api.grocerystorebackend.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.entity.OrderItem;
import org.api.grocerystorebackend.mapper.OrderItemMapper;
import org.api.grocerystorebackend.repository.OrderItemRepository;
import org.api.grocerystorebackend.service.IOrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

@Service
public class OrderItemServiceImpl implements IOrderItemService {
    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Override
    public OrderItemDTO getOrderItemById(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(() -> new EntityNotFoundException("OrderItem Not Found"));
        return orderItemMapper.toDTO(orderItem, orderItem.getOrder().getDeliveryAt());
    }
}
