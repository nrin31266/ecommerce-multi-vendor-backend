package com.vanrin05.app.service.impl;

import com.vanrin05.app.dto.request.CreateReviewRequest;
import com.vanrin05.app.dto.request.UpdateReviewRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.ReviewMapper;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.User;
import com.vanrin05.app.repository.ReviewRepository;
import com.vanrin05.app.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;

    @Override
    public Review createReview(CreateReviewRequest createReviewRequest, User user, Product product) {
        Review review = reviewMapper.toReview(createReviewRequest);
        review.setUser(user);
        review.setProduct(product);
        return reviewRepository.save(review);
    }

    @Override
    public Review updateReview(UpdateReviewRequest updateReviewRequest, Long reviewId, User user) {
        Review review = getReviewById(reviewId);
        if(!review.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "User not authorized to update review");
        }
        reviewMapper.updateReview(review, updateReviewRequest);

        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(()-> new AppException("Review not found"));
    }

    @Override
    public void deleteReview(Long reviewId, User user) {
        Review review = getReviewById(reviewId);
        if(!review.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "User not authorized to delete review");
        }
        reviewRepository.delete(review);
    }


}
