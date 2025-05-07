package com.vanrin05.app.controller;

import com.vanrin05.app.dto.request.CreateReviewRequest;
import com.vanrin05.app.dto.request.UpdateReviewRequest;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.User;
import com.vanrin05.app.service.ReviewService;
import com.vanrin05.app.service.impl.ProductService;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/reviews")
public class ReviewController {
    ReviewService reviewService;
    UserService userService;
    ProductService productService;


    @GetMapping("/{productId}")
    public ResponseEntity<List<Review>> allReviewOfProduct(
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productService.findProductById(productId)));
    }
    @GetMapping("/first/{productId}")
    public ResponseEntity<List<Review>> firstReviewOfProduct(
            @PathVariable Long productId
    ){
        return ResponseEntity.ok(reviewService.getFirstReviewsByProduct(productService.findProductById(productId)));
    }


}
