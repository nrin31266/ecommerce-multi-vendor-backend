package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.SubProductDto;
import com.vanrin05.app.model.product.SubProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubProductMapper {
    @Mapping(target = "productId", source = "product.id")
    SubProductDto toDto(SubProduct subProduct);
}
