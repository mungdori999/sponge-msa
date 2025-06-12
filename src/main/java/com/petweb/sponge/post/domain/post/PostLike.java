package com.petweb.sponge.post.domain.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostLike {

    private Long id;
    private Long postId;
    private Long userId;

    @Builder
    public PostLike(Long id, Long postId, Long userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }


    public static PostLike from(Long postId, Long userId) {
        return PostLike.builder()
                .postId(postId)
                .userId(userId)
                .build();
    }
}
