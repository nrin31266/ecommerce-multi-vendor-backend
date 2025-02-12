package com.vanrin05.controller;

import com.vanrin05.dto.request.CreateReviewRequest;
import com.vanrin05.dto.request.UpdateReviewRequest;
import com.vanrin05.model.Product;
import com.vanrin05.model.Review;
import com.vanrin05.model.User;
import com.vanrin05.service.ReviewService;
import com.vanrin05.service.impl.ProductService;
import com.vanrin05.service.impl.UserService;
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
@RequestMapping("/api")
public class ReviewController {
    ReviewService reviewService;
    UserService userService;
    ProductService productService;

    @GetMapping("/product/{productId}/reviews")
    public ResponseEntity<List<Review>> getReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @PostMapping("/product/{productId}/reviews")
    public ResponseEntity<Review> addReview(@PathVariable Long productId, @RequestBody CreateReviewRequest request, @RequestHeader("Authorization") String jwtToken) {
        Product product = productService.findProductById(productId);
        User user = userService.findUserByJwtToken(jwtToken);

        return ResponseEntity.ok(reviewService.createReview(request,user,product));
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId, @RequestBody UpdateReviewRequest request, @RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserByJwtToken(jwtToken);

        return ResponseEntity.ok(reviewService.updateReview(request, reviewId, user));
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId, @RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserByJwtToken(jwtToken);
        reviewService.deleteReview(reviewId, user);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
