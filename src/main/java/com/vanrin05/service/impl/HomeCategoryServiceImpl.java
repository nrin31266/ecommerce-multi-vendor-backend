package com.vanrin05.service.impl;

import com.vanrin05.exception.AppException;
import com.vanrin05.model.HomeCategory;
import com.vanrin05.repository.HomeCategoryRepository;
import com.vanrin05.service.HomeCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeCategoryServiceImpl implements HomeCategoryService {
    HomeCategoryRepository homeCategoryRepository;

    @Override
    public HomeCategory createHomeCategory(HomeCategory homeCategory) {
        return homeCategoryRepository.save(homeCategory);
    }

    @Override
    public List<HomeCategory> createCategories(List<HomeCategory> homeCategories) {
        if(homeCategoryRepository.findAll().isEmpty()){
            return homeCategoryRepository.saveAll(homeCategories);
        }

        return homeCategoryRepository.findAll();
    }

    @Override
    public HomeCategory updateHomeCategory(HomeCategory homeCategory, Long homeCategoryId) {
        HomeCategory existingHomeCategory = homeCategoryRepository.findById(homeCategoryId).orElseThrow(()-> new AppException("Home category not found"));
        if(homeCategory.getImage() != null){
            existingHomeCategory.setImage(homeCategory.getImage());
        }
        if(homeCategory.getCategoryId() != null){
            existingHomeCategory.setCategoryId(homeCategory.getCategoryId());
        }
        return homeCategoryRepository.save(existingHomeCategory);
    }

    @Override
    public List<HomeCategory> getAllHomeCategories() {
        return homeCategoryRepository.findAll();
    }
}
