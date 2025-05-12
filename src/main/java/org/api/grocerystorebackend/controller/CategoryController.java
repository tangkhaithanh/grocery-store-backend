package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.dto.response.ApiResponse;
import org.api.grocerystorebackend.dto.response.CategoryDTO;
import org.api.grocerystorebackend.entity.Category;
import org.api.grocerystorebackend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getCategories(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "20") int size) {
        try{
            Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
            Page<Category> listCategory = categoryService.getAllCategories(pageable);
            return ResponseEntity.ok(ApiResponse.ok("Lấy danh mục sản phẩm thành công", listCategory));
        }
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.fail("Lỗi khi lấy danh mục sản phẩm"));
        }
    }
}
