package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.AddUpdateBannerRequest;
import com.vanrin05.app.model.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BannerMapper {
    void updateBanner(@MappingTarget Banner banner, AddUpdateBannerRequest rq);
}
