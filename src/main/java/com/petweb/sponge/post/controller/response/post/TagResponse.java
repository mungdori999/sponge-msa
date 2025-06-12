package com.petweb.sponge.post.controller.response.post;

import com.petweb.sponge.post.domain.post.Tag;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TagResponse {

    private Long id;
    private String hashtag;

    public static TagResponse from(Tag tag) {
        return TagResponse.builder()
                .id(tag.getId())
                .hashtag(tag.getHashtag()).build();
    }

}
