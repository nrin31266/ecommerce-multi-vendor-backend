package com.vanrin05.controller;

import com.vanrin05.model.Product;
import com.vanrin05.model.User;
import com.vanrin05.model.Wishlist;
import com.vanrin05.service.WishListService;
import com.vanrin05.service.impl.ProductService;
import com.vanrin05.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {
    WishListService wishListService;
    UserService userService;
    ProductService productService;

    @GetMapping
    public ResponseEntity<Wishlist> getWishlist(@RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserByJwtToken(jwtToken);
        Wishlist wishlist = wishListService.getWishListByUser(user);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping("add-product/{productId}")
    public ResponseEntity<Wishlist> addProduct(@RequestHeader("Authorization") String jwtToken, @PathVariable("productId") Long productId) {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwtToken);
        return ResponseEntity.ok(wishListService.addProductToWishlist(user, product));

    }
}
