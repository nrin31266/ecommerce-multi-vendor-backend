package com.vanrin05.app.service;


import com.vanrin05.app.dto.CartItemDto;
import com.vanrin05.app.dto.request.UpdateCartItemRequest;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.cart.CartItem;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;

public interface CartItemService {

    CartItemDto updateCartItem(UpdateCartItemRequest request, Long cartItemId, User user);
    void removeCartItem(Long userId, Long cartItemId);
    CartItem findCartItemById(Long cartItemId);
}
