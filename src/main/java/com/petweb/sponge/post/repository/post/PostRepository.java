package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    Optional<Post> findById(Long id);
    Optional<Post> findShortById(Long id);
    List<Post> findListByUserId(Long userId, int page);
    List<Post> findListByPostIdList(List<Long> postIdList);

    List<Post> findByKeyword(String keyword, int page);

    List<Post> findListByCode(Long categoryCode, int page);

    Post save(Post post);

    void delete(Post post);

    void initPost(Long id);

}
