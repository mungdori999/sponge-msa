package com.petweb.sponge.post.domain.answer;

import com.petweb.sponge.post.dto.answer.AdoptAnswerCreate;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class AdoptAnswerTest {


    @Test
    public void AdoptAnswerCreate로_ADOPTANSWER를_생성한다() {

        // given
        Long userId = 1L;
        AdoptAnswerCreate adoptAnswerCreate = AdoptAnswerCreate.builder()
                .answerId(1L)
                .trainerId(1L)
                .postId(1L)
                .build();

        // when
        AdoptAnswer result = AdoptAnswer.from(userId, adoptAnswerCreate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAnswerId()).isEqualTo(adoptAnswerCreate.getAnswerId());
        assertThat(result.getTrainerId()).isEqualTo(adoptAnswerCreate.getTrainerId());
        assertThat(result.getUserId()).isEqualTo(userId);
    }
}
