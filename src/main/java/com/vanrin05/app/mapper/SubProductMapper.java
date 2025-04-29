package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.SubProductDto;
import com.vanrin05.app.dto.request.UpdateSubProductReq;
import com.vanrin05.app.model.product.SubProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubProductMapper {
    @Mapping(target = "productId", source = "product.id")
    SubProductDto toDto(SubProduct subProduct);

    @Mapping(target = "options", ignore = true)
    void updateSubProduct(@MappingTarget SubProduct subProduct, UpdateSubProductReq updateSubProductReq);
}
