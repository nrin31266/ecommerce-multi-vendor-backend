package com.vanrin05.app.controller;

import com.vanrin05.app.dto.WishlistItemDto;
import com.vanrin05.app.dto.response.UserWishlistProductResponse;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.User;
import com.vanrin05.app.model.WishlistItem;
import com.vanrin05.app.service.WishListService;
import com.vanrin05.app.service.impl.ProductService;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WishlistController {
    WishListService wishListService;
    UserService userService;
    ProductService productService;

    @GetMapping("/user")
    public ResponseEntity<List<WishlistItemDto>> getWishlist(@RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserByJwtToken(jwtToken);
        List<WishlistItemDto> wishlists = wishListService.getWishListByUser(user);
        return ResponseEntity.ok(wishlists);
    }

    @PostMapping("/user/{productId}")
    public ResponseEntity<UserWishlistProductResponse> addProduct(@RequestHeader("Authorization") String jwtToken, @PathVariable("productId") Long productId) {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwtToken);
        return ResponseEntity.ok(wishListService.addProductToWishlist(user, product));
    }

    @PostMapping("/user/check/{productId}")
    public ResponseEntity<UserWishlistProductResponse> isUserWishList(@RequestHeader("Authorization") String jwtToken, @PathVariable("productId") Long productId) {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwtToken);
        return ResponseEntity.ok(wishListService.isUserWishList(user, product));
    }
}
