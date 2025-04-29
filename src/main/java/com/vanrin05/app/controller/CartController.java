package com.vanrin05.app.controller;

import com.vanrin05.app.dto.CartDto;
import com.vanrin05.app.dto.CartItemDto;
import com.vanrin05.app.dto.request.AddCartRequest;
import com.vanrin05.app.dto.request.UpdateCartItemRequest;
import com.vanrin05.app.model.cart.CartItem;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.product.SubProduct;
import com.vanrin05.app.service.CartItemService;
import com.vanrin05.app.service.CartService;
import com.vanrin05.app.service.impl.ProductService;
import com.vanrin05.app.service.impl.UserService;
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
    public ResponseEntity<CartDto> findUserCart(@RequestHeader("Authorization") String jwt) {
        User user = userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(cartService.getUserCart(user));
    }

    @PutMapping("/add/{productId}/item/{subProductId}")
    public ResponseEntity<CartItemDto> addCartItem(@RequestBody AddCartRequest addCartRequest, @RequestHeader("Authorization") String jwt,
                                                   @PathVariable Long productId, @PathVariable Long subProductId) {
        User user = userService.findUserByJwtToken(jwt);
        Product product = productService.findProductById(productId);
        SubProduct subProduct = productService.findSubProductById(subProductId);
        return ResponseEntity.ok(cartService.addCartItem(user, product, addCartRequest.getQuantity(), subProduct));
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
