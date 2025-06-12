package com.petweb.sponge.trainer.domain;

import com.petweb.sponge.trainer.dto.ReviewCreate;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Review {

    private Long id;

    private int score;
    private String content;

    private Long createdAt;

    private Long trainerId;

    private Long userId;

    @Builder
    public Review(Long id, int score, String content, Long createdAt, Long trainerId, Long userId) {
        this.id = id;
        this.score = score;
        this.content = content;
        this.createdAt = createdAt;
        this.trainerId = trainerId;
        this.userId = userId;
    }

    public static Review from(Long userId, ReviewCreate reviewCreate, ClockHolder clockHolder) {
        return Review.builder()
                .score(reviewCreate.getScore())
                .content(reviewCreate.getContent())
                .trainerId(reviewCreate.getTrainerId())
                .userId(userId)
                .createdAt(clockHolder.clock())
                .build();
    }
}
