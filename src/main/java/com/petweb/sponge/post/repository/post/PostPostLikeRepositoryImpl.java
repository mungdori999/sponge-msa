package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.PostLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PostPostLikeRepositoryImpl implements PostLikeRepository {

    private final PostLikeJpaRepository postLikeJpaRepository;
    @Override
    public Optional<PostLike> findLike(Long postId, Long loginId) {
        return postLikeJpaRepository.findLike(postId, loginId)
                .map(PostLikeEntity::toModel);
    }

    @Override
    public void save(PostLike postLike) {
        postLikeJpaRepository.save(PostLikeEntity.from(postLike));
    }

    @Override
    public void delete(PostLike postLike) {
        postLikeJpaRepository.delete(PostLikeEntity.from(postLike));
    }

    @Override
    public void deleteByPostId(Long postId) {
        postLikeJpaRepository.deleteByPostId(postId);
    }
}
