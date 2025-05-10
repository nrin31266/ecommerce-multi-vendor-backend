package com.vanrin05.app.controller;

import com.vanrin05.app.dto.response.CategoriesResponse;
import com.vanrin05.app.service.impl.CategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/categories")
public class CategoryController {
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoriesResponse> getCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }
}
