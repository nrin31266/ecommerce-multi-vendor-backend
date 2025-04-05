package com.vanrin05.app.service;

import com.vanrin05.app.dto.request.CreateReviewRequest;
import com.vanrin05.app.dto.request.UpdateReviewRequest;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.User;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest createReviewRequest, User user, Product product);
    Review updateReview(UpdateReviewRequest updateReviewRequest, Long reviewId, User user);

    List<Review> getReviewsByProductId(Long productId);

    Review getReviewById(Long reviewId);

    void deleteReview(Long reviewId, User user);
}
