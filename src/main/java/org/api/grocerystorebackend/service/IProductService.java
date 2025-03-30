package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface IProductService {
    Page<ProductDTO> getAllProducts(Pageable pageable);
    Page<ProductDTO> getProductsByCategory(Long categoryId, Pageable pageable);
}
