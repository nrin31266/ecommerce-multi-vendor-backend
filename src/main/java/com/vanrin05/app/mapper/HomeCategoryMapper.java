package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.AddUpdateBannerRequest;
import com.vanrin05.app.dto.request.AddUpdateHomeCategoryRequest;
import com.vanrin05.app.model.Banner;
import com.vanrin05.app.model.HomeCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HomeCategoryMapper {
    @Mapping(target = "categoryIds", ignore = true)
    HomeCategory toHomeCategory(AddUpdateHomeCategoryRequest addUpdateBannerRequest);

    @Mapping(target = "categoryIds", ignore = true)
    void updateHomeCategory(@MappingTarget HomeCategory homeCategory, AddUpdateHomeCategoryRequest addUpdateBannerRequest);
}
