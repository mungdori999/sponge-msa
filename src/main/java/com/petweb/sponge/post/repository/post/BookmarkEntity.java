package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.Bookmark;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "bookmark")
public class BookmarkEntity {
    @Id
    @GeneratedValue
    private Long id;
    private Long postId;
    private Long userId;
    private Long createdAt;
    @Builder
    public BookmarkEntity(Long id, Long postId, Long userId) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
    }

    public static BookmarkEntity from(Bookmark bookmark) {
        BookmarkEntity bookmarkEntity = new BookmarkEntity();
        bookmarkEntity.id = bookmark.getId();
        bookmarkEntity.postId = bookmark.getPostId();
        bookmarkEntity.userId = bookmark.getUserId();
        bookmarkEntity.createdAt = bookmark.getCreatedAt();
        return bookmarkEntity;
    }
    public Bookmark toModel() {
        return Bookmark.builder()
                .id(id)
                .postId(postId)
                .userId(userId)
                .createdAt(createdAt)
                .build();
    }
}
