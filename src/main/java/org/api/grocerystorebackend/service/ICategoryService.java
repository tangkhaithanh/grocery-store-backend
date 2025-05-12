package org.api.grocerystorebackend.service;

import org.api.grocerystorebackend.dto.response.CategoryDTO;
import org.api.grocerystorebackend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICategoryService {
    Page<Category> getAllCategories(Pageable pageable);
}
