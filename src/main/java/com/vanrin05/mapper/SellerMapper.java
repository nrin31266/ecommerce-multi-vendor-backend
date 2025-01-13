package com.vanrin05.mapper;

import com.vanrin05.dto.request.CreateSellerRequest;
import com.vanrin05.dto.request.UpdateSellerRequest;
import com.vanrin05.model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    Seller toSeller(CreateSellerRequest createSellerRequest);
    void updateSeller(@MappingTarget Seller seller, UpdateSellerRequest updateSellerRequest);
}
