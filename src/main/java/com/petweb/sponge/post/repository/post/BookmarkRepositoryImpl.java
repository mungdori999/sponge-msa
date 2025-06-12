package com.petweb.sponge.post.repository.post;

import com.petweb.sponge.post.domain.post.Bookmark;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BookmarkRepositoryImpl implements BookmarkRepository {

    private final BookmarkJpaRepository bookmarkJpaRepository;
    private static int PAGE_SIZE = 10;

    @Override
    public Optional<Bookmark> findBookmark(Long postId, Long loginId) {
        return bookmarkJpaRepository.findBookmark(postId,loginId).map(BookmarkEntity::toModel);
    }

    @Override
    public void save(Bookmark bookmark) {
        bookmarkJpaRepository.save(BookmarkEntity.from(bookmark));
    }

    @Override
    public void deleteByPostId(Long postId) {
        bookmarkJpaRepository.deleteByPostId(postId);
    }

    @Override
    public void delete(Bookmark bookmark) {
        bookmarkJpaRepository.delete(BookmarkEntity.from(bookmark));
    }

    @Override
    public List<Bookmark> findBookmarkList(Long loginId, int page) {
        int offset = page * PAGE_SIZE;
        return bookmarkJpaRepository.findBookmarkList(loginId,PAGE_SIZE,offset).stream().map(BookmarkEntity::toModel).toList();
    }
}
