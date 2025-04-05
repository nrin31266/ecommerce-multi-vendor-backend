package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.request.UpdateCartItemRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.CartMapper;
import com.vanrin05.app.model.CartItem;
import com.vanrin05.app.model.User;
import com.vanrin05.app.repository.CartItemRepository;
import com.vanrin05.app.repository.CartRepository;
import com.vanrin05.app.service.CartItemService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CartItemServiceImpl implements CartItemService {
    CartItemRepository cartItemRepository;
    CartRepository cartRepository;
    CartMapper cartMapper;

    @Override
    public CartItem updateCartItem(Long userId, Long cartItemId, UpdateCartItemRequest updateCartItemRequest) {
//        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
//        User cartItemUser = cartItem.getCart().getUser();
//        if (cartItemUser.getId().equals(userId)) {
//            cartItem.setQuantity(updateCartItemRequest.getQuantity());
//            cartItem.setMrpPrice(cartItem.getQuantity() * cartItem.getProduct().getMrpPrice());
//            cartItem.setSellingPrice(cartItem.getQuantity() * cartItem.getProduct().getSellingPrice());
//            return cartItemRepository.save(cartItem);
//        }else{
//            throw new AppException("You can't update this cart item");
//        }
        return null;
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
        if(cartItem != null && cartItem.getCart().getUser().getId().equals(userId)){
            cartItemRepository.delete(cartItem);
        }else{
            throw new AppException(ErrorCode.UNAUTHORIZED, "You can't remove this cart item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }



}
