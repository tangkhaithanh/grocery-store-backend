package org.api.grocerystorebackend.service.impl;

import jakarta.transaction.Transactional;
import org.api.grocerystorebackend.dto.response.*;
import org.api.grocerystorebackend.entity.*;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.mapper.OrderMapper;
import org.api.grocerystorebackend.repository.OrderRepository;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.repository.ReviewRepository;
import org.api.grocerystorebackend.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Page<OrderDTO> getAllOrdersByStatusAndIDUser(Pageable pageable, StatusOrderType status, Long id) {
        Page<Order> listOrders;
        if(status == StatusOrderType.ALL) {
            listOrders = orderRepository.findAllByUserId(id, pageable);
            return listOrders.map(orderMapper::toDTO);
        }
        else if(status == StatusOrderType.DELIVERED) {
            listOrders = orderRepository.findAllByStatusAndId(status, id, pageable);
            Page<OrderDTO> orders = listOrders.map(orderMapper::toDTO);
            return orders.map( order -> {
                List<Review> reviews = reviewRepository.findByUserIdAndOrderId(id, order.getId());
                Set<Long> reviewedProductIds = reviews.stream()
                        .map(r -> r.getProduct().getId())
                        .collect(Collectors.toSet());
                // Thời điểm hiện tại
                LocalDateTime now = LocalDateTime.now();

                // Kiểm tra xem đơn hàng đã giao có quá 1 tháng chưa
                boolean isWithinOneMonth = order.getDeliveryAt() != null &&
                        order.getDeliveryAt().isAfter(now.minusMonths(1));

                order.getOrderItems().stream()
                    .map(item -> {
                        ProductDTO product = item.getProduct();

                        boolean isReviewed = reviewedProductIds.contains(product.getId());

                        // Sản phẩm chỉ có thể đánh giá nếu chưa đánh giá và trong vòng 1 tháng kể từ khi giao hàng
                        boolean canReview = !isReviewed && isWithinOneMonth;
                        order.getOrderItems().stream().map(
                                orderItem -> {
                                    if(orderItem.getProduct().getId() == product.getId()) {
                                        orderItem.setCanReview(canReview);
                                        orderItem.setReviewed(isReviewed);
                                    }
                                    return orderItem;
                                }
                        ).toList();

                        return null;
                    }).toList();

                return order;
                }
            );
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

    @Override
    public List<DeliveredOrderDTO> getDeliveredOrdersWithReviewStatus(User user) {
        List<Order> orders = orderRepository.findByUserIdAndStatus(user.getId(), StatusOrderType.DELIVERED);

        return orders.stream().map(order -> {
            List<Review> reviews = reviewRepository.findByUserIdAndOrderId(user.getId(), order.getId());
            Set<Long> reviewedProductIds = reviews.stream()
                    .map(r -> r.getProduct().getId())
                    .collect(Collectors.toSet());
            // Thời điểm hiện tại
            LocalDateTime now = LocalDateTime.now();

            // Kiểm tra xem đơn hàng đã giao có quá 1 tháng chưa
            boolean isWithinOneMonth = order.getDeliveryAt() != null &&
                    order.getDeliveryAt().isAfter(now.minusMonths(1));

            List<ProductReviewStatusDTO> products = order.getOrderItems().stream()
                    .map(item -> {
                        Product product = item.getProduct();
                        String thumbnail = product.getImages().stream()
                                .findFirst()
                                .map(ProductImage::getImageUrl)
                                .orElse(null);
                        boolean isReviewed = reviewedProductIds.contains(product.getId());

                        // Sản phẩm chỉ có thể đánh giá nếu chưa đánh giá và trong vòng 1 tháng kể từ khi giao hàng
                        boolean canReview = !isReviewed && isWithinOneMonth;

                        return new ProductReviewStatusDTO(
                                product.getId(),
                                product.getName(),
                                thumbnail,
                                isReviewed,
                                canReview
                        );
                    }).toList();

            return new DeliveredOrderDTO(
                    order.getId(),
                    order.getDeliveryAt(),
                    products
            );
        }).toList();
    }
}
