package com.petweb.sponge.post.controller.response.post;

import com.petweb.sponge.post.domain.post.PostFile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostFileResponse {

    private Long id;
    private String fileUrl;

    public static  PostFileResponse from(PostFile postFile) {
        return PostFileResponse.builder()
                .id(postFile.getId())
                .fileUrl(postFile.getFileUrl())
                .build();
    }
}
