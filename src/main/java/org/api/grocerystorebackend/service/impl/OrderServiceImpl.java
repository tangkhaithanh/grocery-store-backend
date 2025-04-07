package org.api.grocerystorebackend.service.impl;

import jakarta.transaction.Transactional;
import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.entity.OrderItem;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.mapper.OrderMapper;
import org.api.grocerystorebackend.repository.OrderRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Page<OrderDTO> getAllOrdersByStatusAndIDUser(Pageable pageable, StatusOrderType status, Long id) {
        Page<Order> listOrders;
        if(status == StatusOrderType.ALL) {
            listOrders = orderRepository.findAllByUserId(id, pageable);
            return listOrders.map(orderMapper::toDTO);
        }
        listOrders = orderRepository.findAllByStatusAndId(status, id, pageable);
        return listOrders.map(orderMapper::toDTO);
    }

    @Transactional
    @Override
    public Boolean cancelOrder(Long userID, long orderID) {
        //Lấy order theo mã Order và mã User
        Order order = orderRepository.findByUserIdAndId(userID, orderID);
        if (order == null || order.getOrderItems() == null) {
            return false;
        }

        // Lặp qua từng OrderItem để hoàn trả số lượng vào kho
        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            int quantity = item.getQuantity();

            // Cập nhật lại số lượng trong kho
            product.setQuantity(product.getQuantity() + quantity);

            // Lưu thay đổi sản phẩm
            productRepository.save(product);
        }

        // Đặt trạng thái đơn hàng là "cancelled" hoặc tương đương
        order.setStatus(StatusOrderType.CANCELED);
        orderRepository.save(order);
        return true;
    }
}
