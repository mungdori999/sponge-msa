package com.petweb.sponge.post.dto.answer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerCreate {

    private Long postId;
    private String content;

}
