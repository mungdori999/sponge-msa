package com.petweb.sponge.post.domain.post;

import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;


@Getter
public class Bookmark {

    private Long id;
    private Long postId;
    private Long userId;
    private Long createdAt;

    @Builder
    public Bookmark(Long id, Long postId, Long userId, Long createdAt) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public static Bookmark from(Long postId, Long loginId,ClockHolder clockHolder) {
        return Bookmark.builder()
                .postId(postId)
                .userId(loginId)
                .createdAt(clockHolder.clock())
                .build();
    }
}
