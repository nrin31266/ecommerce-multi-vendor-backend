package com.vanrin05.service;

import com.vanrin05.model.Cart;
import com.vanrin05.model.CartItem;
import com.vanrin05.model.Product;
import com.vanrin05.model.User;

public interface CartService {
    public CartItem addCartItem(User user, Product product, String size, int quantity);

    public Cart findUserCart(User user);
}
