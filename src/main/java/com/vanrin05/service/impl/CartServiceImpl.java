package com.vanrin05.service.impl;

import com.vanrin05.exception.AppException;
import com.vanrin05.model.Cart;
import com.vanrin05.model.CartItem;
import com.vanrin05.model.Product;
import com.vanrin05.model.User;
import com.vanrin05.repository.CartItemRepository;
import com.vanrin05.repository.CartRepository;
import com.vanrin05.service.CartService;
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


    @Override
    public CartItem addCartItem(User user, Product product, String size, int quantity) {
        Cart cart = cartRepository.findByUserId(user.getId());
        Optional<CartItem> cartItemOptional = cartItemRepository.findByCartAndProductAndSize(cart, product, size);
        if(cartItemOptional.isEmpty()){
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setSize(size);
            cartItem.setQuantity(quantity);
            cartItem.setUserId(user.getId());

            cartItem.setSellingPrice(product.getSellingPrice() * quantity);
            cartItem.setMrpPrice(product.getMrpPrice() * quantity);

            cart.getCartItems().add(cartItem);
            cartItem.setCart(cart);

            return cartItemRepository.save(cartItem);
        }else{
            return cartItemOptional.get();
        }
    }

    @Override
    public Cart findUserCart(User user) {
        Cart cart = cartRepository.findByUserId(user.getId());

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItems = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalItems += cartItem.getQuantity();
            totalPrice += cartItem.getMrpPrice();
            totalDiscountedPrice += cartItem.getSellingPrice();
        }

        cart.setTotalItems(totalItems);
        cart.setTotalSellingPrice(totalDiscountedPrice);
        cart.setTotalMrpPrice(totalPrice);
        cart.setDiscount(discountPercentage(totalPrice, totalDiscountedPrice));

        return cart;
    }

    private int discountPercentage(double mrpPrice, double sellingPrice) {

        if (mrpPrice < sellingPrice) {
            throw new AppException("Mrp price is invalid. Mrp: " + mrpPrice + ", Selling price: " + sellingPrice);
        }
        double discount = (mrpPrice - sellingPrice);
        double percentage = discount / sellingPrice * 100;
        return (int) percentage;
    }
}
