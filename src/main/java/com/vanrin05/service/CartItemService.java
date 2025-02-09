package com.vanrin05.service;


import com.vanrin05.dto.request.UpdateCartItemRequest;
import com.vanrin05.model.CartItem;

public interface CartItemService {
    CartItem updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest updateCartItem);
    void removeCartItem(Long userId, Long cartItemId);
    CartItem findCartItemById(Long cartItemId);
}
