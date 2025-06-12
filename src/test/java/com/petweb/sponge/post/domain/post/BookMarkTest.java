package com.petweb.sponge.post.domain.post;

import com.petweb.sponge.utils.TestClockHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class BookMarkTest {


    @Test
    public void Bookmark를_생성할_수_있다() {

        // given
        Long postId =1L;
        Long userId =1L;

        // when
        Bookmark result = Bookmark.from(postId, userId,new TestClockHolder(12345L));

        // then
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getPostId()).isEqualTo(1L);
        assertThat(result.getCreatedAt()).isEqualTo(12345L);
    }
}
