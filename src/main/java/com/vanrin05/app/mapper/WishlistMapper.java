package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.WishlistItemDto;
import com.vanrin05.app.model.WishlistItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WishlistMapper {
    WishlistItemDto toWishlistItemDto(WishlistItem wishlistItem);
}
