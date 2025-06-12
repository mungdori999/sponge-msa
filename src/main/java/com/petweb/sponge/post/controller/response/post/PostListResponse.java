package com.petweb.sponge.post.controller.response.post;

import com.petweb.sponge.post.domain.post.Post;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class PostListResponse {
    private Long id;
    private String title;
    private String content;
    private Long createdAt;
    private int likeCount;
    private int answerCount;
    private Long userId;
    private List<PostCategoryResponse> postCategoryList;

    public static PostListResponse from(Post post) {
        return PostListResponse.builder()
                .id(post.getId())
                .title(post.getPostContent().getTitle())
                .content(post.getPostContent().getContent())
                .createdAt(post.getPostContent().getCreatedAt())
                .likeCount(post.getLikeCount())
                .answerCount(post.getAnswerCount())
                .userId(post.getUserId())
                .postCategoryList(post.getPostCategoryList().stream().map(PostCategoryResponse::from).collect(Collectors.toList()))
                .build();


    }
}
