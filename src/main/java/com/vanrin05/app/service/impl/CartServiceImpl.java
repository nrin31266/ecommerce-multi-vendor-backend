package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.CartDto;
import com.vanrin05.app.dto.CartItemDto;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.mapper.CartItemMapper;
import com.vanrin05.app.mapper.CartMapper;
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

import java.util.Optional;

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
        if(!product.getSubProducts().contains(subProduct)) {
            throw new AppException("Product does not have a sub product");
        }

        Cart cart = cartRepository.findByUserId(user.getId());
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProductAndSubProduct(cart, product, subProduct);
        if(cartItemOptional.isEmpty()){


            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setSubProduct(subProduct);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());


            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemMapper.toCartItemDto( cartItemRepository.save(cartItem));
        }else{
            if(quantity <= 0){
                throw new AppException("Quantity should be greater than 0");
            }

            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);

            return cartItemMapper.toCartItemDto( cartItemRepository.save(cartItem));
        }
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        calculateCartSummary(cart);
        return cart;
    }

    @Override
    public CartDto getUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());
        calculateCartSummary(cart);
        return cartMapper.toCartDto(cart);
    }
    private void calculateCartSummary(Cart cart) {
        long totalPrice = 0L;
        long totalDiscountedPrice = 0L;
        int totalItems = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            int quantity = cartItem.getQuantity();
            long mrp = cartItem.getSubProduct().getMrpPrice();
            long selling = cartItem.getSubProduct().getSellingPrice();

            totalItems += quantity;
            totalPrice += quantity * mrp;
            totalDiscountedPrice += quantity * selling;
        }

        cart.setTotalItems(totalItems);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setTotalMrpPrice(totalPrice);
        cart.setDiscount(discountPercentage(totalPrice, totalDiscountedPrice));
    }




    private int discountPercentage(double mrpPrice, double sellingPrice) {

        if (mrpPrice < sellingPrice) {
            throw new AppException("Mrp price is invalid. Mrp: " + mrpPrice + ", Selling price: " + sellingPrice);
        }
        double discount = (mrpPrice - sellingPrice);
        double percentage = discount / mrpPrice * 100;
        return (int) percentage;
    }
}
