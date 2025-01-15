package com.vanrin05.mapper;

import com.vanrin05.dto.request.CreateProductReq;
import com.vanrin05.dto.request.UpdateProductReq;
import com.vanrin05.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toProduct(CreateProductReq createProductReq);
    void updateProduct(@MappingTarget Product product, UpdateProductReq updateProductReq);
}
