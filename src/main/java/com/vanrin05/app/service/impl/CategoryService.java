package com.vanrin05.app.service.impl;

import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.repository.CategoryRepository;
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

    public Category findByCategoryId(String categoryId) {
        return categoryRepository.findByCategoryId(categoryId).orElseThrow(()-> new AppException("Category not found"));
    }
}
