package org.api.grocerystorebackend.mapper;

import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.dto.response.ProductSimpleDTO;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.enums.StatusOrderType;
import org.api.grocerystorebackend.service.IPricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductMapper {
    @Autowired
    private IPricingService pricingService;

    public ProductDTO toDTO(Product product) {
        List<String> imageUrls = product.getImages().stream()
                .map(img -> img.getImageUrl())
                .toList();

        double avgRating = 0.0;
        if (product.getOrderItems() != null) {
            avgRating = product.getOrderItems().stream()
                    .filter(r -> r.getReview() != null) // Lọc ra các OrderItem đã được review
                    .mapToInt(r -> r.getReview().getRating())
                    .average()
                    .orElse(0.0);
        }


        int soldCount = product.getOrderItems().stream()
                .filter(item -> item.getOrder() != null &&
                        item.getOrder().getStatus() == StatusOrderType.DELIVERED)
                .mapToInt(item -> item.getQuantity())
                .sum();

        BigDecimal effectivePrice = pricingService.getEffectivePrice(product);

       return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getQuantity(),
                product.getStatus(),
                product.getCreatedAt(),
                product.getUpdatedAt(),
                product.getCategory().getName(),
                product.getCategory().getImageUrl(),
                imageUrls,
                avgRating,
                soldCount,
                effectivePrice
        );
    }
    public ProductSimpleDTO toSimpleDTO(Product product) {
        List<String> imageUrls = product.getImages().stream()
                .map(img -> img.getImageUrl())
                .toList();

        return new ProductSimpleDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                imageUrls.get(0)
        );

    }
}
