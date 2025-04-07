package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.repository.ProductRepository;
import org.api.grocerystorebackend.service.IProductService;
import org.api.grocerystorebackend.utils.ConvertDTOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

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

    @Override
    public Page<ProductDTO> getProductsSortedByPriceAsc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceAsc(pageable).map(ConvertDTOUtil::mapToProductDTO);
    }

    @Override
    public Page<ProductDTO> getProductsSortedByPriceDesc(Pageable pageable) {
        return productRepository.findAllByOrderByPriceDesc(pageable).map(ConvertDTOUtil::mapToProductDTO);
    }

    @Override
    public Page<ProductDTO> getProductsSortedByCreatedAt(Pageable pageable) {
        return productRepository.findAllByOrderByCreatedAtDesc(pageable).map(ConvertDTOUtil::mapToProductDTO);
    }

    @Override
    public Page<ProductDTO> getProductsSortedByRating(Pageable pageable) {
        return productRepository.findAllOrderByAverageRatingDesc(pageable).map(ConvertDTOUtil::mapToProductDTO);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(ConvertDTOUtil::mapToProductDTO)
                .orElse(null);
    }

    @Override
    public Page<ProductDTO> getBestSellersLast7Days(Pageable pageable) {
        LocalDateTime startDate= LocalDateTime.now().minusDays(7);

        Page<Product> productPage= productRepository.findBestSellingLast7Days(startDate, pageable);

        List<ProductDTO> dtoList = productPage.stream()
                .map(ConvertDTOUtil::mapToProductDTO)
                .filter(dto -> dto.getSoldCount() > 100)
                .toList();
        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
}
