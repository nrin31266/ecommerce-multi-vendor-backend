package com.vanrin05.app.service;

import com.vanrin05.app.dto.request.CreateReviewRequest;
import com.vanrin05.app.dto.request.UpdateReviewRequest;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.User;

import java.util.List;

public interface ReviewService {
    Review createReview(CreateReviewRequest createReviewRequest, User user, Long orderItemId);


    List<Review> getReviewsByProduct(Product product);
    List<Review> getFirstReviewsByProduct(Product product);

    Review getReviewById(Long reviewId);


}
