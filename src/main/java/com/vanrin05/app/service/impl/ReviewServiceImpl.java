package com.vanrin05.app.service.impl;

import com.vanrin05.app.domain.SELLER_ORDER_STATUS;
import com.vanrin05.app.dto.request.CreateReviewRequest;
import com.vanrin05.app.dto.request.UpdateReviewRequest;
import com.vanrin05.app.exception.AppException;
import com.vanrin05.app.exception.ErrorCode;
import com.vanrin05.app.mapper.ReviewMapper;
import com.vanrin05.app.model.orderpayment.OrderItem;
import com.vanrin05.app.model.product.Product;
import com.vanrin05.app.model.Review;
import com.vanrin05.app.model.User;
import com.vanrin05.app.repository.ReviewRepository;
import com.vanrin05.app.service.OrderService;
import com.vanrin05.app.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    ReviewMapper reviewMapper;
    OrderService orderService;



    @Transactional
    @Override
    public Review createReview(CreateReviewRequest createReviewRequest, User user, Long orderItemId) {
        OrderItem orderItem = orderService.findOrderItemById(orderItemId);
        if(!user.getId().equals(orderItem.getUserId())){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if(createReviewRequest.getReviewRating() < 1 || createReviewRequest.getReviewRating() > 5){
            throw new AppException("Review rating should be between 1 and 5");
        }
        if(orderItem.getIsRated() != null && orderItem.getIsRated()){
            throw new AppException("You rated this product");
        }
        if(orderItem.getSellerOrder().getStatus() != SELLER_ORDER_STATUS.COMPLETED){
            throw new AppException("You can not rate this product");
        }
        orderItem.setIsRated(true);
        orderService.saveOrderItem(orderItem);
        Review review = reviewMapper.toReview(createReviewRequest);
        review.setUser(user);
        Product product = orderItem.getProduct();
        int oldCount = product.getNumberRating();
        double oldAvg = product.getAvgRating();

        int newCount = oldCount + 1;
        double newAvg = ((oldAvg * oldCount) + createReviewRequest.getReviewRating()) / newCount;

        product.setNumberRating(newCount);
        product.setAvgRating(newAvg);

        review.setProduct(product);
        review.setSubProduct(orderItem.getSubProduct());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProduct(product);
    }

    @Override
    public List<Review> getFirstReviewsByProduct(Product product) {
        Pageable pageable = PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<Review> review = reviewRepository.findByProduct(product, pageable);
        return review;
    }

    @Override
    public Review getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(()-> new AppException("Review not found"));
    }



}
