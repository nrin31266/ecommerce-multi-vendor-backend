package com.vanrin05.app.service;

import com.vanrin05.app.domain.BANNER_TARGET_TYPE;
import com.vanrin05.app.dto.request.AddUpdateBannerRequest;
import com.vanrin05.app.model.Banner;

import java.util.List;

public interface BannerService {
    Banner addBanner(Banner banner);
    Banner updateBanner(Integer id, AddUpdateBannerRequest u);
    void deleteBanner(Integer id);
    List<Banner> getAllActiveBanners();
    List<Banner> getByTargetType(BANNER_TARGET_TYPE bannerTargetType);
    List<Banner> getAllBanners();
}
