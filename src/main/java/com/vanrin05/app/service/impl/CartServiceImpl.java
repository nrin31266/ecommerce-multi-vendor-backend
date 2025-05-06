package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.CartDto;
import com.vanrin05.app.dto.CartItemDto;
import com.vanrin05.app.dto.response.ShopCartGroupResponse;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.CartItemMapper;
import com.vanrin05.app.mapper.CartMapper;
import com.vanrin05.app.model.Seller;
import com.vanrin05.app.model.cart.Cart;
import com.vanrin05.app.model.cart.CartItem;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.product.SubProduct;
import com.vanrin05.app.repository.CartItemRepository;
import com.vanrin05.app.repository.CartRepository;
import com.vanrin05.app.service.CartService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    CartItemRepository cartItemRepository;
    CartItemMapper cartItemMapper;
    CartMapper cartMapper;

    @Override
    public CartItemDto addCartItem(User user, Product product, int quantity, SubProduct subProduct) {
        if(quantity <= 0){
            throw new AppException("Quantity should be greater than 0");
        }

        if(!product.getSubProducts().contains(subProduct)) {
            throw new AppException("Product does not have a sub product");
        }

        Cart cart = cartRepository.findByUserId(user.getId());
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProductAndSubProduct(cart, product, subProduct);
        if(cartItemOptional.isEmpty()){
            if(quantity > subProduct.getQuantity()){
                throw new AppException("Out of stock. Available quantity is " + subProduct.getQuantity());
            }

            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setSubProduct(subProduct);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());


            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemMapper.toCartItemDto( cartItemRepository.save(cartItem));
        }else{
            CartItem cartItem = cartItemOptional.get();
            if(quantity + cartItem.getQuantity()  > subProduct.getQuantity()){
                throw new AppException("Out of stock. You already have " + cartItem.getQuantity() +
                                       " in your cart. Only " + subProduct.getQuantity() + " available in stock.");
            }


            cartItem.setQuantity(quantity + cartItem.getQuantity());
            cartItem.setCart(cart);

            return cartItemMapper.toCartItemDto( cartItemRepository.save(cartItem));
        }
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        return cart;
    }

    @Override
    public CartDto getUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        List<CartItem> cartItems = cart.getCartItems();

        // Nhóm CartItem theo Seller
        Map<Seller, List<CartItem>> groupedBySeller = cartItems.stream()
                .collect(Collectors.groupingBy(
                        cartItem -> cartItem.getProduct().getSeller()
                ));

        // Chuyển đổi sang List<ShopCartGroupResponse>
        List<ShopCartGroupResponse> groups = groupedBySeller.entrySet().stream()
                .map(entry -> new ShopCartGroupResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Map sang DTO
        CartDto cartDto = cartMapper.toCartDto(cart);
        cartDto.setGroups(groups);

        return cartDto;
    }





}
