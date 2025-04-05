package org.api.grocerystorebackend.repository;

import org.api.grocerystorebackend.dto.response.CategoryDTO;
import org.api.grocerystorebackend.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
