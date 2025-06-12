package com.petweb.sponge.post.domain.post;

import lombok.Builder;
import lombok.Getter;


@Getter
public class PostContent {

    private String title;
    private String content;
    private String duration;
    private Long createdAt;
    private Long modifiedAt;

    @Builder
    public PostContent(String title, String content, String duration, Long createdAt,Long modifiedAt) {
        this.title = title;
        this.content = content;
        this.duration = duration;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
