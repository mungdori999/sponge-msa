package com.petweb.sponge.post.controller.response.answer;

import com.petweb.sponge.post.controller.response.post.PostCheckResponse;
import com.petweb.sponge.post.domain.answer.AnswerLike;
import com.petweb.sponge.post.domain.post.Bookmark;
import com.petweb.sponge.post.domain.post.PostLike;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Builder
public class AnswerCheckResponse {

    private boolean likeCheck;

    public static AnswerCheckResponse from(Optional<AnswerLike> like) {
        boolean likeFlag = like.isPresent();
        return AnswerCheckResponse.builder()
                .likeCheck(likeFlag)
                .build();
    }
}
