package com.petweb.sponge.post.domain.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostCategory {
    private Long id;
    private Long categoryCode;

    @Builder
    public PostCategory(Long id, Long categoryCode) {
        this.id = id;
        this.categoryCode = categoryCode;
    }

    public static PostCategory from(Long categoryCode) {
        return PostCategory.builder()
                .categoryCode(categoryCode).build();
    }
}
