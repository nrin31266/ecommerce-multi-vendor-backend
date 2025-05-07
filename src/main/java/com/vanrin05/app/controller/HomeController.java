package com.vanrin05.app.controller;

import com.vanrin05.app.dto.response.ApiResponse;
import com.vanrin05.app.dto.response.HomeCategoryResponse;
import com.vanrin05.app.model.Banner;
import com.vanrin05.app.model.HomeCategory;
import com.vanrin05.app.repository.HomeCategoryRepository;
import com.vanrin05.app.service.BannerService;
import com.vanrin05.app.service.HomeCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeController {
    HomeCategoryService homeCategoryService;
    BannerService bannerService;

    @GetMapping("/categories")
    public ResponseEntity<HomeCategoryResponse> getHomeCategories() {
        return ResponseEntity.ok(homeCategoryService.getAllHomeCategories());
    }

    @GetMapping("/banners")
    public ResponseEntity<List<Banner>> getHomeBanners() {
        return ResponseEntity.ok(bannerService.getAllActiveBanners());
    }

}
