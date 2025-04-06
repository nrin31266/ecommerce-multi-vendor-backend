package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.CartItemDto;
import com.vanrin05.app.model.cart.CartItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartItemMapper {
    CartItemDto toCartItemDto(CartItem cartItem);
}
