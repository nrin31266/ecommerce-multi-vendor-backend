package com.vanrin05.service;


import com.vanrin05.model.HomeCategory;

import java.awt.*;
import java.util.List;

public interface HomeCategoryService {
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(HomeCategory homeCategory, Long homeCategoryId);
    List<HomeCategory> getAllHomeCategories();

}
