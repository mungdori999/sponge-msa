package com.petweb.sponge.post.repository.answer;

import com.petweb.sponge.post.domain.answer.AnswerLike;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "answer_like")
public class AnswerLikeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long answerId;

    private Long userId;

    public AnswerLike toModel() {
        return AnswerLike.builder()
                .id(id)
                .answerId(answerId)
                .userId(userId)
                .build();
    }

    public static AnswerLikeEntity from(AnswerLike answerLike) {
        AnswerLikeEntity answerLikeEntity = new AnswerLikeEntity();
        answerLikeEntity.id = answerLike.getId();
        answerLikeEntity.answerId = answerLike.getAnswerId();
        answerLikeEntity.userId = answerLike.getUserId();
        return answerLikeEntity;
    }
}
