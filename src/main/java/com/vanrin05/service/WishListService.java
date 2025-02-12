package com.vanrin05.service;

import com.vanrin05.model.Product;
import com.vanrin05.model.User;
import com.vanrin05.model.Wishlist;

public interface WishListService {
    Wishlist getWishListByUser(User user);
    Wishlist createWishList(User user);
    Wishlist addProductToWishlist(User user, Product product);
}
