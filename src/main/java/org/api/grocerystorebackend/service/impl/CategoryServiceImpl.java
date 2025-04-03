package org.api.grocerystorebackend.service.impl;

import org.api.grocerystorebackend.entity.Category;
import org.api.grocerystorebackend.repository.CategoryRepository;
import org.api.grocerystorebackend.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
