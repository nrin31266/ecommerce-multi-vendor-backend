package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.HOME_CATEGORY_SECTION;
import com.vanrin05.app.dto.request.AddUpdateHomeCategoryRequest;
import com.vanrin05.app.dto.response.HomeCategoryResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.HomeCategoryMapper;
import com.vanrin05.app.model.Category;
import com.vanrin05.app.model.HomeCategory;
import com.vanrin05.app.repository.HomeCategoryRepository;
import com.vanrin05.app.service.HomeCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeCategoryServiceImpl implements HomeCategoryService {
    HomeCategoryRepository homeCategoryRepository;
    CategoryService categoryService;
    HomeCategoryMapper homeCategoryMapper;
    @Override
    public HomeCategory createHomeCategory(AddUpdateHomeCategoryRequest rq) {
        if(rq.getCategoryIds().isEmpty()){
            throw new AppException("Category is required, limit 1 category");
        }
        HomeCategory homeCategory = homeCategoryMapper.toHomeCategory(rq);
        homeCategory.setCategoryIds(String.join(",", rq.getCategoryIds()));
        return homeCategoryRepository.save(homeCategory);
    }


    @Override
    public HomeCategory updateHomeCategory(AddUpdateHomeCategoryRequest rq, Long homeCategoryId) {
        HomeCategory existingHomeCategory = homeCategoryRepository.findById(homeCategoryId).orElseThrow(()-> new AppException("Home category not found"));
        homeCategoryMapper.updateHomeCategory(existingHomeCategory, rq);
        existingHomeCategory.setCategoryIds(String.join(",", rq.getCategoryIds()));
        return homeCategoryRepository.save(existingHomeCategory);
    }

    @Override
    public HomeCategoryResponse getAllHomeCategories() {
        List<HomeCategory> homeCategories = homeCategoryRepository.findAll();

        Map<HOME_CATEGORY_SECTION, List<HomeCategory>> grouped = homeCategories.stream()
                .collect(Collectors.groupingBy(HomeCategory::getHomeCategorySection));

        return HomeCategoryResponse.builder()
                .electronics(grouped.getOrDefault(HOME_CATEGORY_SECTION.ELECTRIC_CATEGORY, Collections.emptyList()))
                .men(grouped.getOrDefault(HOME_CATEGORY_SECTION.MEN_CATEGORY, Collections.emptyList()))
                .women(grouped.getOrDefault(HOME_CATEGORY_SECTION.WOMEN_CATEGORY, Collections.emptyList()))
                .homeFurniture(grouped.getOrDefault(HOME_CATEGORY_SECTION.HOME_FURNITURE_CATEGORY, Collections.emptyList()))
                .build();
    }

    @Override
    public List<HomeCategory> getHomeCategoriesByType(HOME_CATEGORY_SECTION type) {
        return homeCategoryRepository.findByHomeCategorySection(type);
    }

    @Override
    public void deleteHomeCategory(Long homeCategoryId) {
        homeCategoryRepository.findById(homeCategoryId).ifPresent(homeCategoryRepository::delete);
    }

    @Override
    public List<HomeCategory> getAll() {
        return homeCategoryRepository.findAll();
    }
}
