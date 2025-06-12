package com.petweb.sponge.post.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostLikeJpaRepository extends JpaRepository<PostLikeEntity,Long> {


    @Query("SELECT l FROM PostLikeEntity l WHERE l.postId = :postId AND l.userId = :loginId")
    Optional<PostLikeEntity> findLike(@Param("postId") Long postId, @Param("loginId") Long loginId);
    @Query("DELETE PostLikeEntity pe where pe.postId= :postId")
    @Modifying
    void deleteByPostId(@Param("postId") Long postId);
}
