package com.petweb.sponge.post.domain.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostFile {

    private Long id;
    private String fileUrl;

    @Builder
    public PostFile(Long id, String fileUrl) {
        this.id = id;
        this.fileUrl = fileUrl;
    }

    public static PostFile from(String fileUrl) {
        return PostFile.builder().fileUrl(fileUrl).build();
    }
}
