package com.petweb.sponge.post.dto.answer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdoptAnswerCreate {

    private Long answerId;
    private Long trainerId;
    private Long postId;
}
