package com.petweb.sponge.post.domain.answer;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AnswerLikeTest {


    @Test
    public void AnswerLike를_생성한다() {

        // given
        Long answerId = 1L;
        Long userId = 1L;

        // when
        AnswerLike result = AnswerLike.from(answerId, userId);

        // then
        assertThat(result.getAnswerId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
    }
}
