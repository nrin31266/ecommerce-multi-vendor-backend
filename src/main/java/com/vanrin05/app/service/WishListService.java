package com.vanrin05.app.service;

import com.vanrin05.app.model.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.Wishlist;

public interface WishListService {
    Wishlist getWishListByUser(User user);
    Wishlist createWishList(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
