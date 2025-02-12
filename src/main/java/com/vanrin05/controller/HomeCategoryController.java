package com.vanrin05.controller;

import com.vanrin05.model.Home;
import com.vanrin05.model.HomeCategory;
import com.vanrin05.service.HomeCategoryService;
import com.vanrin05.service.HomeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeCategoryController {
    HomeCategoryService homeCategoryService;
    HomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategory(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> categories= homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return ResponseEntity.ok(home);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategory() {
        return ResponseEntity.ok(homeCategoryService.getAllHomeCategories());
    }

    @PutMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id, @RequestBody HomeCategory homeCategory) {
        return ResponseEntity.ok(homeCategoryService.updateHomeCategory(homeCategory, id));
    }
}
