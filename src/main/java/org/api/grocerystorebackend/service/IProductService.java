package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<ProductDTO> getAllProducts(Pageable pageable);
    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);

    // Filter Method:

    Page<ProductDTO> getProductsSortedByPriceAsc(Pageable pageable);
    Page<ProductDTO> getProductsSortedByPriceDesc(Pageable pageable);
    Page<ProductDTO> getProductsSortedByCreatedAt(Pageable pageable);
    Page<ProductDTO> getProductsSortedByRating(Pageable pageable);

    ProductDTO getProductById(Long id);

    // Lấy sản phẩm bán chạy nhất trong vòng 7 ngày:
    Page<ProductDTO>getBestSellersLast7Days(Pageable pageable);
}
