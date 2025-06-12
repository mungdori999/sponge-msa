package com.petweb.sponge.trainer.domain;

import com.petweb.sponge.trainer.dto.ReviewCreate;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ReviewTest {

    @Test
    public void reviewCreate로_REVIEW를_생성한다() {
        // given
        Long userId =1L;
        ReviewCreate reviewCreate = ReviewCreate.builder()
                .trainerId(1L)
                .score(3)
                .content("별로에요").build();

        // when
        Review result = Review.from(userId, reviewCreate, new TestClockHolder(12345L));

        // then
        assertThat(result.getUserId()).isEqualTo(userId);
        assertThat(result.getTrainerId()).isEqualTo(reviewCreate.getTrainerId());
        assertThat(result.getScore()).isEqualTo(reviewCreate.getScore());
        assertThat(result.getContent()).isEqualTo(reviewCreate.getContent());
        assertThat(result.getCreatedAt()).isEqualTo(12345L); // TestClockHolder에서 설정한 값

    }

}
