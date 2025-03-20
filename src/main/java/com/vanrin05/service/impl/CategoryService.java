package com.vanrin05.service.impl;

import com.vanrin05.model.Category;
import com.vanrin05.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CategoryService {
    CategoryRepository categoryRepository;

    public Category findOrCreateCategory(String categoryId, Category parentCategory, int level) {
        Optional<Category> existingCategory = categoryRepository.findByCategoryId(categoryId);
        if (existingCategory.isPresent()) {
            return existingCategory.get();
        }

        Category newCategory = new Category();
        newCategory.setLevel(level);
        newCategory.setCategoryId(categoryId);
        newCategory.setParentCategory(parentCategory.getCategoryId());
        return categoryRepository.save(newCategory);
    }
}
