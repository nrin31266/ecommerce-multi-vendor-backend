package com.vanrin05.app.controller;

import com.vanrin05.app.dto.request.CreateReviewRequest;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.User;
import com.vanrin05.app.service.ReviewService;
import com.vanrin05.app.service.impl.ProductService;
import com.vanrin05.app.service.impl.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/reviews/users")
public class UserReviewController {
    ReviewService reviewService;
    UserService userService;
    ProductService productService;



    @PostMapping("/{orderItemId}")
    public ResponseEntity<Review> addReview(@PathVariable Long orderItemId, @RequestBody CreateReviewRequest request, @RequestHeader("Authorization") String jwtToken) {
        User user = userService.findUserByJwtToken(jwtToken);
        return ResponseEntity.ok(reviewService.createReview(request,user,orderItemId));
    }



}
