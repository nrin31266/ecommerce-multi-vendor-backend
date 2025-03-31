package com.vanrin05.app.service;

import com.vanrin05.app.model.Cart;
import com.vanrin05.app.model.CartItem;
import com.vanrin05.app.model.Product;
import com.vanrin05.app.model.User;

public interface CartService {
    public CartItem addCartItem(User user, Product product, String size, int quantity);

    public Cart findUserCart(User user);
}
