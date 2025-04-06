package com.vanrin05.app.service;

import com.vanrin05.app.dto.CartDto;
import com.vanrin05.app.dto.CartItemDto;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.product.SubProduct;

public interface CartService {
    public CartItemDto addCartItem(User user, Product product, int quantity, SubProduct subProduct);

    public CartDto findUserCart(User user);


}
