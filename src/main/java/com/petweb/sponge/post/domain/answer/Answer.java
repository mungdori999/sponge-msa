package com.petweb.sponge.post.domain.answer;

import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.post.dto.answer.AnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerUpdate;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.parameters.P;


@Getter
public class Answer {

    private Long id;
    private String content; // 내용
    private int likeCount; // 추천수
    private Long createdAt;
    private Long modifiedAt;
    private Long postId;
    private Long trainerId;

    @Builder
    public Answer(Long id, String content, int likeCount, Long createdAt, Long modifiedAt, Long postId, Long trainerId) {
        this.id = id;
        this.content = content;
        this.likeCount = likeCount;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.postId = postId;
        this.trainerId = trainerId;
    }

    public static Answer from(Long trainerId, AnswerCreate answerCreate, ClockHolder clockHolder) {
        return Answer.builder()
                .content(answerCreate.getContent())
                .postId(answerCreate.getPostId())
                .trainerId(trainerId)
                .createdAt(clockHolder.clock())
                .modifiedAt(0L)
                .build();
    }

    public Answer update(AnswerUpdate answerUpdate, ClockHolder clockHolder) {
        return Answer.builder()
                .id(id)
                .content(answerUpdate.getContent())
                .likeCount(likeCount)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .postId(postId)
                .trainerId(trainerId)
                .modifiedAt(clockHolder.clock())
                .build();
    }

    public void checkTrainer(Long loginId) {
        if (!trainerId.equals(loginId)) {
            throw new LoginIdError();
        }
    }

    public void decreaseLikeCount() {
        if (likeCount <= 0) {
            throw new IllegalStateException();
        } else {
            likeCount--;
        }
    }

    public void increaseLikeCount() {
        likeCount++;
    }
}
