package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.entity.OrderItem;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.repository.OrderRepository;
import org.api.grocerystorebackend.service.IOrderService;
import org.api.grocerystorebackend.utils.ConvertDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;


    @Override
    public Page<OrderDTO> getAllOrdersByStatusAndIDUser(Pageable pageable, StatusOrderType status, Long id) {
        Page<Order> listOrders;
        if(status == StatusOrderType.ALL) {
            listOrders = orderRepository.findAllByUserId(id, pageable);
            return listOrders.map(ConvertDTOUtil::mapToOrderDTO);
        }
        listOrders = orderRepository.findAllByStatusAndId(status, id, pageable);
        return listOrders.map(ConvertDTOUtil::mapToOrderDTO);

    }


}
