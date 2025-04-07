package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.OrderItem;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.entity.ProductImage;
import org.api.grocerystorebackend.entity.Review;
import org.api.grocerystorebackend.enums.OrderStatus;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.service.IProductService;
import org.api.grocerystorebackend.utils.ConvertDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;


    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(ConvertDTOUtil::mapToProductDTO);
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        return productPage.map(ConvertDTOUtil::mapToProductDTO);
    }

<<<<<<< HEAD

=======
    @Override
    public Page<ProductDTO> getProductsSortedByPriceAsc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceAsc(pageable).map(this::mapToDto);
    }

    @Override
    public Page<ProductDTO> getProductsSortedByPriceDesc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceDesc(pageable).map(this::mapToDto);
    }

    @Override
    public Page<ProductDTO> getProductsSortedByCreatedAt(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::mapToDto);
    }

    @Override
    public Page<ProductDTO> getProductsSortedByRating(Pageable pageable) {
        return productRepository.findAllOrderByAverageRatingDesc(pageable).map(this::mapToDto);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToDto)
                .orElse(null);
    }

    @Override
    public Page<ProductDTO> getBestSellersLast7Days(Pageable pageable) {
        LocalDateTime startDate= LocalDateTime.now().minusDays(7);

        Page<Product> productPage= productRepository.findBestSellingLast7Days(startDate, pageable);

        List<ProductDTO> dtoList = productPage.stream()
                .map(this::mapToDto)
                .filter(dto -> dto.getSoldCount() > 100)
                .toList();
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }

    private ProductDTO mapToDto(Product product) {
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
                        item.getOrder().getStatus() == OrderStatus.COMPLETED)
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
>>>>>>> dev_thanh
}
