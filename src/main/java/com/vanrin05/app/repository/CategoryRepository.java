package com.vanrin05.app.repository;

import com.vanrin05.app.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByCategoryId(String categoryId);
    List<Category> findAllByCategoryIdIn(List<String> categoryIds);
}
