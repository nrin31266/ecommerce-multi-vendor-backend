package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.AddressRequest;
import com.vanrin05.app.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address toAddress(AddressRequest addressRequest);
    void updateAddress(@MappingTarget Address address, AddressRequest addressRequest);
}
