package com.vanrin05.controller;

import com.vanrin05.dto.request.AddCartRequest;
import com.vanrin05.dto.request.UpdateCartItemRequest;
import com.vanrin05.model.Cart;
import com.vanrin05.model.CartItem;
import com.vanrin05.model.Product;
import com.vanrin05.model.User;
import com.vanrin05.service.CartItemService;
import com.vanrin05.service.CartService;
import com.vanrin05.service.impl.ProductService;
import com.vanrin05.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;
    CartItemService cartItemService;
    UserService userService;
    ProductService productService;

    @GetMapping
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(cartService.findUserCart(user));
    }

    @PutMapping("/add")
    public ResponseEntity<CartItem> addCartItem(@RequestBody AddCartRequest addCartRequest, @RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(addCartRequest.getProductId());
        return ResponseEntity.ok(cartService.addCartItem(user, product, addCartRequest.getSize(), addCartRequest.getQuantity()));
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@RequestHeader("Authorization") String jwt,
                                               @PathVariable Long cartItemId) {
        User user = userService.findUserByJwtToken(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/item/{cartItemId}")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody UpdateCartItemRequest updateCartItemRequest,
                                                   @RequestHeader("Authorization") String jwt,
                                                   @PathVariable Long cartItemId) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(cartItemService.updateCartItem(user.getId(), cartItemId, updateCartItemRequest));
    }
}
