package com.vanrin05.app.mapper;

import com.vanrin05.app.dto.request.CreateReviewRequest;
import com.vanrin05.app.dto.request.UpdateReviewRequest;
import com.vanrin05.app.model.Review;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    Review toReview(CreateReviewRequest createReviewRequest);
    void updateReview(@MappingTarget Review review, UpdateReviewRequest updateReviewRequest);
}
