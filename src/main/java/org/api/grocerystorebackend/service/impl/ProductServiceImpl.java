package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;


    @Override
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(this::mapToDto);
    }

    @Override
    public Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productPage = productRepository.findByCategoryId(categoryId, pageable);
        return productPage.map(this::mapToDto);
    }

    private ProductDTO mapToDto(Product product) {
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
}
