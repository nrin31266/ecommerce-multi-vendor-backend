package com.vanrin05.app.service;


import com.vanrin05.app.dto.request.UpdateCartItemRequest;
import com.vanrin05.app.model.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest updateCartItem);
    void removeCartItem(Long userId, Long cartItemId);
    CartItem findCartItemById(Long cartItemId);
}
