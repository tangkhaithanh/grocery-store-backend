package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.dto.response.CategoryDTO;
import org.api.grocerystorebackend.dto.response.ProductDTO;
import org.api.grocerystorebackend.entity.Category;
import org.api.grocerystorebackend.entity.Product;
import org.api.grocerystorebackend.repository.CategoryRepository;
import org.api.grocerystorebackend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Override
    public Page<Category> getAllCategories(Pageable pageable) {
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        return categoryPage;
    }



}
