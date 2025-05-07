package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.BANNER_TARGET_TYPE;
import com.vanrin05.app.dto.request.AddUpdateBannerRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.BannerMapper;
import com.vanrin05.app.model.Banner;
import com.vanrin05.app.repository.BannerRepository;
import com.vanrin05.app.service.BannerService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BannerServiceImpl implements BannerService {
    BannerRepository bannerRepository;
    BannerMapper bannerMapper;

    @Override
    public Banner addBanner(Banner banner) {
        return bannerRepository.save(banner);
    }

    @Override
    public Banner updateBanner(Integer id, AddUpdateBannerRequest u) {
        Banner b = bannerRepository.findById(id).orElseThrow(()->new AppException("Banner not found"));
        bannerMapper.updateBanner(b, u);
        return null;
    }

    @Override
    public void deleteBanner(Integer id) {
        Optional<Banner> b = bannerRepository.findById(id);
        b.ifPresent(bannerRepository::delete);
    }

    @Override
    public List<Banner> getAllActiveBanners() {
        return bannerRepository.findActiveBanners();
    }

    @Override
    public List<Banner> getByTargetType(BANNER_TARGET_TYPE bannerTargetType) {
        return bannerRepository.findByTargetType(bannerTargetType);
    }

    @Override
    public List<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }
}
