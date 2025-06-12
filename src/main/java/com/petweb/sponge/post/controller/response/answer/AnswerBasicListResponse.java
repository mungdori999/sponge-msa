package com.petweb.sponge.post.controller.response.answer;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class AnswerBasicListResponse {

    private AnswerResponse answerResponse;
    private boolean checkAdopt;

    public static AnswerBasicListResponse from(AnswerResponse answerResponse,  boolean checkAdopt) {
        return AnswerBasicListResponse.builder()
                .answerResponse(answerResponse)
                .checkAdopt(checkAdopt)
                .build();
    }
}
