package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.DeliveredOrderDTO;
import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.entity.User;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Page<OrderDTO> getAllOrdersByStatusAndIDUser(Pageable pageable, StatusOrderType status, Long id);

    Boolean cancelOrder(Long userID, long orderID);
    OrderDTO findById(Long id);
}
