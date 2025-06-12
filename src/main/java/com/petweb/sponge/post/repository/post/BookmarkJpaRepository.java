package com.petweb.sponge.post.repository.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookmarkJpaRepository extends JpaRepository<BookmarkEntity, Long> {


    @Query("SELECT be FROM BookmarkEntity be WHERE be.postId = :postId AND be.userId = :loginId")
    Optional<BookmarkEntity> findBookmark(@Param("postId") Long postId, @Param("loginId") Long loginId);

    @Query(value = "SELECT * FROM BOOKMARK be WHERE be.user_id = :userId ORDER BY be.created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<BookmarkEntity> findBookmarkList(@Param("userId") Long loginId, @Param("limit") int limit, @Param("offset") int offset);

    @Query("DELETE BookmarkEntity be where be.postId= :postId")
    @Modifying
    void deleteByPostId(@Param("postId") Long postId);
}
