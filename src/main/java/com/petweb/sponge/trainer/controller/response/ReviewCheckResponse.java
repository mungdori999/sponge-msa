package com.petweb.sponge.trainer.controller.response;

import com.petweb.sponge.trainer.domain.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class ReviewCheckResponse {

    private boolean reviewCheck;

    public static ReviewCheckResponse from(Optional<Review> review) {
        boolean reviewFlag = review.isPresent();
        return ReviewCheckResponse.builder()
                .reviewCheck(reviewFlag)
                .build();
    }
}
