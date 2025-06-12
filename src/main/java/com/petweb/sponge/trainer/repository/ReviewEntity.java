package com.petweb.sponge.trainer.repository;

import com.petweb.sponge.trainer.domain.Review;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "review")
public class ReviewEntity {

    @Id
    @GeneratedValue
    private Long id;

    private int score;
    private String content;

    private Long createdAt;

    private Long trainerId;

    private Long userId;

    @Builder
    public ReviewEntity(Long id, int score, String content, Long createdAt, Long trainerId, Long userId) {
        this.id = id;
        this.score = score;
        this.content = content;
        this.trainerId = trainerId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public static ReviewEntity from(Review review) {
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.id = review.getId();
        reviewEntity.score = review.getScore();
        reviewEntity.content = review.getContent();
        reviewEntity.createdAt = review.getCreatedAt();
        reviewEntity.trainerId = review.getTrainerId();
        reviewEntity.userId = review.getUserId();
        return reviewEntity;
    }

    public Review toModel() {
        return Review.builder()
                .id(id)
                .score(score)
                .content(content)
                .createdAt(createdAt)
                .trainerId(trainerId)
                .userId(userId)
                .build();
    }
}
