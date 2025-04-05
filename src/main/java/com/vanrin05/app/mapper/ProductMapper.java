package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.CreateProductReq;
import com.vanrin05.app.dto.request.UpdateProductReq;
import com.vanrin05.app.model.product.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(ignore = true, target = "optionsTypes")
    Product toProduct(CreateProductReq createProductReq);
    void updateProduct(@MappingTarget Product product, UpdateProductReq updateProductReq);
}
