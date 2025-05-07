package com.vanrin05.app.mapper;

import com.vanrin05.app.model.HomeCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface HomeCategoryMapper {

    @Mapping(target = "id", ignore = true)
    void updateHomeCategory(@MappingTarget HomeCategory homeCategory, HomeCategory rq);
}
