package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.response.CategoriesResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CategoryService {
    CategoryRepository categoryRepository;

    public Category findByCategoryId(String categoryId) {
        return categoryRepository.findByCategoryId(categoryId).orElseThrow(()-> new AppException("Category not found"));
    }

    public CategoriesResponse getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        Map<Integer, List<Category>> categoryMap = categories.stream()
                .collect(Collectors.groupingBy(Category::getLevel));

        List<Category> l1 = categoryMap.get(1);
        List<Category> l2 = categoryMap.get(2);
        List<Category> l3 = categoryMap.get(3);

        Map<String, List<Category>> m2 = l2.stream().collect(Collectors.groupingBy(Category::getParentCategory));
        Map<String, List<Category>> m3 = l3.stream()
                .collect(Collectors.groupingBy(Category::getParentCategory));

        Map<String, List<Category>> m3bym1 = new HashMap<>();

        for (Category level1 : l1) {
            String level1Id = level1.getCategoryId();

            // Lấy danh sách cấp 2 thuộc về cấp 1 này
            List<Category> level2Children = m2.getOrDefault(level1Id, new ArrayList<>());

            // Duyệt từng cấp 2 để gom các cấp 3 tương ứng
            List<Category> level3Descendants = new ArrayList<>();
            for (Category level2 : level2Children) {
                String level2Id = level2.getCategoryId();
                List<Category> level3Children = m3.getOrDefault(level2Id, new ArrayList<>());
                level3Descendants.addAll(level3Children);
            }

            // Gán vào map kết quả
            m3bym1.put(level1Id, level3Descendants);
        }

        return CategoriesResponse.builder()
                .one(categoryMap.get(1))
                .two(categoryMap.get(2))
                .three(categoryMap.get(3))
                .m2(m2)
                .m3(m3)
                .m3bym1(m3bym1)
                .build();
    }
}
