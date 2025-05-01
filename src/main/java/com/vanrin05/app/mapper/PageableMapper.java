package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.PageableDto;
import com.vanrin05.app.dto.ProductDto;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;


@Mapper(componentModel = "spring")
public interface PageableMapper {
    PageableDto<ProductDto> toPageableDto(Page<ProductDto> page);
}