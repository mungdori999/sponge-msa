package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {
    private final PostJpaRepository postJpaRepository;
    private final BookmarkJpaRepository bookmarkJpaRepository;
    private final PostLikeRepository postLikeRepository;

    @Override
    public Optional<Post> findById(Long id) {
        return postJpaRepository.findPostById(id).map(PostEntity::toModel);
    }

    @Override
    public Optional<Post> findShortById(Long id) {
        return postJpaRepository.findById(id).map(PostEntity::toModel);
    }

    @Override
    public List<Post> findListByUserId(Long userId, int page) {
        return postJpaRepository.findListByUserId(userId, page).stream().map(PostEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Post> findListByPostIdList(List<Long> postIdList) {
        return postJpaRepository.findListByIdList(postIdList).stream().map(PostEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Post> findByKeyword(String keyword, int page) {
        return postJpaRepository.findListByKeyword(keyword, page).stream().map(PostEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Post> findListByCode(Long categoryCode, int page) {
        return postJpaRepository.findListByCode(categoryCode, page).stream().map(PostEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Post save(Post post) {
        return postJpaRepository.save(PostEntity.from(post)).toModel();
    }

    @Override
    public void delete(Post post) {
        postJpaRepository.delete(PostEntity.from(post));
    }

    @Override
    public void initPost(Long id) {
        postJpaRepository.initPost(id);
    }


}
