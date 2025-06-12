package com.petweb.sponge.post.domain.answer;

import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.post.dto.answer.AnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerUpdate;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.*;

public class AnswerTest {


    @Test
    public void AnswerCreate로_ANSWER를_생성한다() {
        // given
        Long trainerId = 1L;
        AnswerCreate answerCreate = AnswerCreate.builder()
                .postId(1L)
                .content("테스트 내용입니다")
                .build();
        // when
        Answer result = Answer.from(trainerId, answerCreate, new TestClockHolder(12345L));

        // then
        assertThat(result.getPostId()).isEqualTo(1L);
        assertThat(result.getTrainerId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("테스트 내용입니다");
        assertThat(result.getCreatedAt()).isEqualTo(12345L);
    }

    @Test
    public void answerUpdate로_ANSWER를_수정한다() {
        // given
        Answer answer = Answer.builder()
                .content("테스트 내용입니다")
                .createdAt(0L)
                .modifiedAt(0L)
                .postId(1L)
                .trainerId(1L)
                .build();
        AnswerUpdate answerUpdate = AnswerUpdate.builder()
                .content("수정 내용입니다").build();

        // when
        Answer result = answer.update(answerUpdate, new TestClockHolder(12345L));

        // then
        assertThat(result.getContent()).isEqualTo("수정 내용입니다");
    }

    @Test
    public void checkTrainer로_현재로그인한_훈련사인지_체크한다() {
        // given
        Answer answer = Answer.builder()
                .content("테스트 내용입니다")
                .createdAt(0L)
                .modifiedAt(0L)
                .postId(1L)
                .trainerId(1L)
                .build();

        // when
        // then
        assertThatThrownBy(() -> answer.checkTrainer(2L))
                .isInstanceOf(LoginIdError.class);

        assertThatCode(() -> answer.checkTrainer(1L))
                .doesNotThrowAnyException();

    }


    @Test
    public void 좋아요_수를_증가시킨다() {
        // given
        Answer answer = Answer.builder()
                .content("테스트 내용입니다")
                .createdAt(0L)
                .modifiedAt(0L)
                .postId(1L)
                .trainerId(1L)
                .build();

        // when
        answer.increaseLikeCount();

        // then
        assertThat(answer.getLikeCount()).isEqualTo(1);
    }

    @Test
    public void 좋아요_수를_감소시킨다() {
        // given
        Answer answer = Answer.builder()
                .content("테스트 내용입니다")
                .createdAt(0L)
                .modifiedAt(0L)
                .postId(1L)
                .trainerId(1L)
                .likeCount(1)
                .build();


        // when
        answer.decreaseLikeCount();

        // then
        assertThat(answer.getLikeCount()).isEqualTo(0);
    }

    @Test
    public void 감소시킬_좋아요가_없으면_오류를_보낸다() {
        // given
        Answer answer = Answer.builder()
                .content("테스트 내용입니다")
                .createdAt(0L)
                .modifiedAt(0L)
                .postId(1L)
                .trainerId(1L)
                .build();


        // when
        // then
        assertThatThrownBy(answer::decreaseLikeCount).isInstanceOf(IllegalStateException.class);
    }




}
