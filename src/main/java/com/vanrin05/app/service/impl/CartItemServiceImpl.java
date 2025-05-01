package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.CartItemDto;
import com.vanrin05.app.dto.request.UpdateCartItemRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.CartItemMapper;
import com.vanrin05.app.mapper.CartMapper;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.cart.CartItem;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.product.SubProduct;
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
    CartItemMapper cartItemMapper;


    @Override
    public CartItemDto updateCartItem(UpdateCartItemRequest request, Long cartItemId, User user) {


        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(() ->
                new AppException("Cart item not found"));
        SubProduct subProduct = cartItem.getSubProduct();

        if (!user.getId().equals(cartItem.getUserId())) {
            cartItem.setUserId(user.getId());
        }
        if (request.getQuantity() < 0 || request.getQuantity() > subProduct.getQuantity()) {
            throw new AppException("Quantity is invalid");
        }

        cartItem.setQuantity(request.getQuantity());
        return cartItemMapper.toCartItemDto(cartItemRepository.save(cartItem));

    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findCartItemById(cartItemId);
        if (cartItem != null && cartItem.getCart().getUser().getId().equals(userId)) {
            cartItemRepository.delete(cartItem);
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED, "You can't remove this cart item");
        }
    }

    @Override
    public CartItem findCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId).orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));
    }


}
