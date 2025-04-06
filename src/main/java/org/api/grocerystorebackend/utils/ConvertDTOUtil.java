package org.api.grocerystorebackend.utils;

import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.Order;
import org.api.grocerystorebackend.entity.OrderItem;
import org.api.grocerystorebackend.entity.Product;

import java.util.List;

public class ConvertDTOUtil {
    public static ProductDTO mapToProductDTO(Product product) {
        List<String> imageUrls = product.getImages().stream()
                .map(img -> img.getImageUrl())
                .toList();

        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getDiscount(),
                product.getQuantity(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategory().getName(),
                product.getCategory().getImageUrl(),
                imageUrls
        );
    }
    public static OrderDTO mapToOrderDTO(Order order) {
        List<OrderItemDTO> listOrderItemsDTO = order.getOrderItems().stream()
                .map(ConvertDTOUtil::mapToOrderItemDTO).toList();
        return new OrderDTO(
                order.getId(),
                order.getTotalAmount(),
                order.getDeliveryAt(),
                listOrderItemsDTO
                );

    }

    public static OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                mapToProductDTO(orderItem.getProduct())
                );
    }
}
