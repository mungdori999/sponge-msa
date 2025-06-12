package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.PostLike;

import java.util.Optional;

public interface PostLikeRepository {
    Optional<PostLike> findLike(Long postId, Long loginId);

    void save(PostLike postLike);
    void delete(PostLike postLike);
    void deleteByPostId(Long postId);

}
