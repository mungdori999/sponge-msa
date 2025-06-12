package com.petweb.sponge.post.domain.answer;

import com.petweb.sponge.post.dto.answer.AdoptAnswerCreate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AdoptAnswer {

    private Long id;
    private Long trainerId;
    private Long userId;
    private Long answerId;

    @Builder
    public AdoptAnswer(Long id, Long trainerId, Long userId, Long answerId) {
        this.id = id;
        this.trainerId = trainerId;
        this.userId = userId;
        this.answerId = answerId;
    }

    public static AdoptAnswer from(Long userId, AdoptAnswerCreate adoptAnswerCreate) {
        return AdoptAnswer.builder()
                .trainerId(adoptAnswerCreate.getTrainerId())
                .userId(userId)
                .answerId(adoptAnswerCreate.getAnswerId())
                .build();
    }

}
