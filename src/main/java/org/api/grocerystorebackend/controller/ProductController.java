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

    @GetMapping("/sorted")
    public ResponseEntity<ApiResponse<?>> getSortedProducts(
            @RequestParam(defaultValue = "default") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20")int size
    ){
        try
        {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> result;
            switch (sortBy.toLowerCase())
            {
                case "price_asc":
                    result = productService.getProductsSortedByPriceAsc(pageable);
                    break;
                case "price_desc":
                    result = productService.getProductsSortedByPriceDesc(pageable);
                    break;
                case "newest":
                    result = productService.getProductsSortedByCreatedAt(pageable);
                    break;
                case "rating":
                    result = productService.getProductsSortedByRating(pageable);
                    break;
                default:
                    result = productService.getAllProducts(pageable);
            }
            return ResponseEntity.ok(ApiResponse.ok("Lọc sản phẩm thành công", result));
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lọc sản phẩm"));
        }
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<ApiResponse<?>> getProductDetail(@PathVariable Long id){
        try {
            ProductDTO productDTO = productService.getProductById(id);
            if (productDTO != null) {
                return ResponseEntity.ok(ApiResponse.ok("Lấy chi tiết sản phẩm thành công", productDTO));
            } else {
                return ResponseEntity.status(404).body(ApiResponse.fail("Không tìm thấy sản phẩm với ID = " + id));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy chi tiết sản phẩm"));
        }
    }

    // Best Selling Product in 7 days ago
    @GetMapping("/best-seller")
    public ResponseEntity<ApiResponse<?>> getBestSeller(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> dtoPage = productService.getBestSellersLast7Days(pageable);
            return ResponseEntity.ok(ApiResponse.ok("Lấy sản phẩm bán chạy thành công", dtoPage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy sản phẩm bán chạy"));
        }
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<?>> searchProductsByname(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> dtoPage = productService.searchProductsByName(name, pageable);

            if (dtoPage.isEmpty()) {
                return ResponseEntity.ok(ApiResponse.ok("Không tìm thấy sản phẩm nào phù hợp", dtoPage));
            }

            return ResponseEntity.ok(ApiResponse.ok("Tìm kiếm sản phẩm thành công", dtoPage));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi tìm kiếm sản phẩm"));
        }
    }
    /*@GetMapping("/featured")
    public ResponseEntity<ApiResponse<?>> getFeaturedProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductDTO> featuredProducts = productService.getFeaturedProducts(pageable);
            return ResponseEntity.ok(ApiResponse.ok("Lấy sản phẩm nổi bật thành công", featuredProducts));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy sản phẩm nổi bật"));
        }
    }*/
}
