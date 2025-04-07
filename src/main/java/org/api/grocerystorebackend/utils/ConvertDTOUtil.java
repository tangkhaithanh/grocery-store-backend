package org.api.grocerystorebackend.utils;

import org.api.grocerystorebackend.dto.response.OrderDTO;
import org.api.grocerystorebackend.dto.response.OrderItemDTO;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.*;
import org.api.grocerystorebackend.enums.StatusOrderType;

import java.util.List;

public class ConvertDTOUtil {
    public static ProductDTO mapToProductDTO(Product product) {
        List<String> imageUrls = product.getImages().stream()
                .map(ProductImage::getImageUrl)
                .toList();

        // Tính toán đánh giá trung bình
        double avgRating = 0.0;
        if (product.getReviews() != null && !product.getReviews().isEmpty()) {
            avgRating = product.getReviews().stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(0.0);
        }

        // Tính số lượt bán:
        int soldCount = product.getOrderItems().stream()
                .filter(item -> item.getOrder() != null &&
                        item.getOrder().getStatus() == StatusOrderType.DELIVERED)
                .mapToInt(OrderItem::getQuantity)
                .sum();

        ProductDTO dto = new ProductDTO(
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
                imageUrls,
                avgRating,
                soldCount
        );
        return dto;
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
