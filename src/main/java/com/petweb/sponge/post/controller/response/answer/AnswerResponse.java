package com.petweb.sponge.post.controller.response.answer;

import com.petweb.sponge.post.domain.answer.Answer;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class AnswerResponse {
    private Long id;
    private String content; // 내용
    private int likeCount; // 추천수
    private Long createdAt;
    private Long modifiedAt;
    private Long postId;
    private Long trainerId;
    public static AnswerResponse from(Answer answer) {
        return AnswerResponse.builder()
                .id(answer.getId())
                .content(answer.getContent())
                .likeCount(answer.getLikeCount())
                .createdAt(answer.getCreatedAt())
                .modifiedAt(answer.getModifiedAt())
                .postId(answer.getPostId())
                .trainerId(answer.getTrainerId())
                .build();
    }
 }
