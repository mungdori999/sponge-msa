package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.PostLike;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "post_like")
public class PostLikeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Long postId;

    private Long userId;

    @Builder
    public PostLikeEntity(Long id, Long postId, Long userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }

    public PostLike toModel() {
        return PostLike.builder()
                .id(id)
                .postId(postId)
                .userId(userId)
                .build();
    }

    public static PostLikeEntity from(PostLike postLike) {
        PostLikeEntity postLikeEntity = new PostLikeEntity();
        postLikeEntity.id = postLike.getId();
        postLikeEntity.postId= postLike.getPostId();
        postLikeEntity.userId = postLike.getUserId();
        return postLikeEntity;
    }
}
