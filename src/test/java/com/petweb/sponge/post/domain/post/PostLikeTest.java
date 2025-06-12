package com.petweb.sponge.post.domain.post;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PostLikeTest {


    @Test
    public void POSTLIKE를_생성할_수_있다() {
        // given
        Long postId = 1L;
        Long userId = 1L;

        // when
        PostLike result = PostLike.from(postId, userId);

        // then
        assertThat(result.getPostId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
    }
}
