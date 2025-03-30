package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    // Lấy toàn bộ sản phẩm
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "20") int size){
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ProductDTO> dtoPage = productService.getAllProducts(pageable);
            return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách sản phẩm thành công", dtoPage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy danh sách sản phẩm"));
        }
    }

    // Lấy sản phẩm theo danh mục (categoryID)
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<ApiResponse<?>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            Page<ProductDTO> dtoPage = productService.getProductsByCategory(categoryId, pageable);
            return ResponseEntity.ok(ApiResponse.ok("Lấy sản phẩm theo danh mục thành công", dtoPage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy sản phẩm theo danh mục"));
        }
    }
}
