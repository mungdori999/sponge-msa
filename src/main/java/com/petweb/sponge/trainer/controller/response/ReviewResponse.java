package com.petweb.sponge.trainer.controller.response;

import com.petweb.sponge.trainer.domain.Review;
import com.petweb.sponge.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@Builder
public class ReviewResponse {

    private Long id;
    private int score;
    private String content;
    private Long createdAt;
    private String userName;

    public static ReviewResponse from(Review review, User user) {
        return ReviewResponse.builder()
                .id(review.getId())
                .score(review.getScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .userName(user.getName())
                .build();
    }
}
