package com.vanrin05.app.service;


import com.vanrin05.app.model.HomeCategory;

import java.util.List;

public interface HomeCategoryService {
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long homeCategoryId);
    List<HomeCategory> getAllHomeCategories();

}
