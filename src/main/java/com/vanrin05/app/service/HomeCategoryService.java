package com.vanrin05.app.service;


import com.vanrin05.app.domain.HOME_CATEGORY_SECTION;
import com.vanrin05.app.dto.response.HomeCategoryResponse;
import com.vanrin05.app.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long homeCategoryId);
    HomeCategoryResponse getAllHomeCategories();
    List<HomeCategory> getHomeCategoriesByType(HOME_CATEGORY_SECTION type);
    void deleteHomeCategory(Long homeCategoryId);
    List<HomeCategory> getAll();
}
