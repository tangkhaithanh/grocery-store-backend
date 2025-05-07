package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.OrderItemDTO;

public interface IOrderItemService {
    OrderItemDTO getOrderItemById(Long orderItemId);
}
