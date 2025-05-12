package com.vanrin05.app.service;

import com.vanrin05.app.dto.WishlistItemDto;
import com.vanrin05.app.dto.response.UserWishlistProductResponse;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.WishlistItem;

import java.util.List;

public interface WishListService {
    List<WishlistItemDto> getWishListByUser(User user);

    UserWishlistProductResponse addProductToWishlist(User user, Product product);

    UserWishlistProductResponse isUserWishList(User user, Product product);
}
