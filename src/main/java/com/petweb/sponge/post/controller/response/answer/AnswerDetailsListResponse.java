package com.petweb.sponge.post.controller.response.answer;

import lombok.Getter;

@Getter
public class AnswerDetailsListResponse extends AnswerBasicListResponse{

    private TrainerShortResponse trainerShortResponse;

    AnswerDetailsListResponse(AnswerResponse answerResponse, TrainerShortResponse trainerShortResponse, boolean checkAdopt) {
        super(answerResponse, checkAdopt);
        this.trainerShortResponse= trainerShortResponse;
    }

    public static AnswerDetailsListResponse from (AnswerResponse answerResponse, TrainerShortResponse trainerShortResponse, boolean checkAdopt) {
        return new AnswerDetailsListResponse(answerResponse,trainerShortResponse,checkAdopt);
    }
}
