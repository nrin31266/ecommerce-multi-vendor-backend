package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.CreateSellerRequest;
import com.vanrin05.app.dto.request.UpdateSellerRequest;
import com.vanrin05.app.model.Seller;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    Seller toSeller(CreateSellerRequest createSellerRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSeller(@MappingTarget Seller seller, UpdateSellerRequest updateSellerRequest);

}
