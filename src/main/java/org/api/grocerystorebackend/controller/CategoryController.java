package org.api.grocerystorebackend.controller;

import org.api.grocerystorebackend.entity.Category;
import org.api.grocerystorebackend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    ICategoryService categoryService;
    @GetMapping
    public List<Category> getCategories() {
        List<Category> listCategory = categoryService.getAllCategories();
        return listCategory;
    }
}
