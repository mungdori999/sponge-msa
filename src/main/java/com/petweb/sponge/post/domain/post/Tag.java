package com.petweb.sponge.post.domain.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Tag {

    private Long id;
    private String hashtag;

    @Builder
    public Tag(Long id, String hashtag) {
        this.id = id;
        this.hashtag = hashtag;
    }

    public static Tag from(String hashtag) {
        return Tag.builder()
                .hashtag(hashtag).build();
    }
}
