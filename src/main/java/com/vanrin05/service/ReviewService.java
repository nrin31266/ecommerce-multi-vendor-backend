package com.vanrin05.service;

import com.vanrin05.dto.request.CreateReviewRequest;
import com.vanrin05.dto.request.UpdateReviewRequest;
import com.vanrin05.model.Product;
import com.vanrin05.model.Review;
import com.vanrin05.model.User;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest createReviewRequest, User user, Product product);
    Review updateReview(UpdateReviewRequest updateReviewRequest, Long reviewId, User user);

    List<Review> getReviewsByProductId(Long productId);

    Review getReviewById(Long reviewId);

    void deleteReview(Long reviewId, User user);
}
