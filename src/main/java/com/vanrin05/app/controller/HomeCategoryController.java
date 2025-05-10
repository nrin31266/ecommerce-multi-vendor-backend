package com.vanrin05.app.controller;

import com.vanrin05.app.domain.HOME_CATEGORY_SECTION;
import com.vanrin05.app.dto.request.AddUpdateHomeCategoryRequest;
import com.vanrin05.app.dto.response.ApiResponse;
import com.vanrin05.app.model.Home;
import com.vanrin05.app.model.HomeCategory;
import com.vanrin05.app.service.HomeCategoryService;
import com.vanrin05.app.service.HomeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home/categories")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HomeCategoryController {
    HomeCategoryService homeCategoryService;

    @PostMapping
    public ResponseEntity<HomeCategory> createHomeCategory(@RequestBody AddUpdateHomeCategoryRequest rq) {
        HomeCategory rs= homeCategoryService.createHomeCategory(rq);
        return ResponseEntity.ok(rs);
    }


    @PutMapping("/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable Long id, @RequestBody AddUpdateHomeCategoryRequest rq) {
        return ResponseEntity.ok(homeCategoryService.updateHomeCategory(rq, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteHomeCategory(@PathVariable Long id) {
        homeCategoryService.deleteHomeCategory(id);
        return ResponseEntity.ok(ApiResponse.builder()
                        .message("Deleted")
                .build());
    }

    @GetMapping
    public ResponseEntity<List<HomeCategory>> getAllHomeCategories(
            @RequestParam(required = false) HOME_CATEGORY_SECTION section
    ) {
        return ResponseEntity.ok(
                section == null ? homeCategoryService.getAll() :
                        homeCategoryService.getHomeCategoriesByType(section)
        );
    }
}
