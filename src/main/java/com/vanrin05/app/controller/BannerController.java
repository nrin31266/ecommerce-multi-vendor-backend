package com.vanrin05.app.controller;

import com.vanrin05.app.dto.request.AddUpdateBannerRequest;
import com.vanrin05.app.dto.response.ApiResponse;
import com.vanrin05.app.dto.response.HomeCategoryResponse;
import com.vanrin05.app.model.Banner;
import com.vanrin05.app.service.BannerService;
import com.vanrin05.app.service.HomeCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/banners")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerController {
    BannerService bannerService;

   @PostMapping
    public ResponseEntity<Banner> createBanner(@RequestBody Banner banner) {
       return ResponseEntity.ok(bannerService.addBanner(banner));
   }

   @PutMapping("/{id}")
    public ResponseEntity<Banner> updateBanner(@RequestBody AddUpdateBannerRequest rq, @PathVariable Integer id) {
       return ResponseEntity.ok(bannerService.updateBanner(id, rq));
   }
   @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBanner(@PathVariable Integer id) {
       bannerService.deleteBanner(id);
       return ResponseEntity.ok(
               ApiResponse.builder()
                       .message("Deleted").build()
       );
   }

   @GetMapping
    public ResponseEntity<List<Banner>> getAllBanners() {
       return ResponseEntity.ok(bannerService.getAllBanners());
   }
}
