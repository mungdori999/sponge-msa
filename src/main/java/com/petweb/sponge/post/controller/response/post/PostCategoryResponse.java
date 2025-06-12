package com.petweb.sponge.post.controller.response.post;

import com.petweb.sponge.post.domain.post.PostCategory;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostCategoryResponse {

    private Long id;
    private Long categoryCode;

    public static PostCategoryResponse from(PostCategory postCategory) {
        return PostCategoryResponse.builder()
                .id(postCategory.getId())
                .categoryCode(postCategory.getCategoryCode())
                .build();
    }

}
