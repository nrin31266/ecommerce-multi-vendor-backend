package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.CartDto;
import com.vanrin05.app.model.cart.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto(Cart cart);
}
