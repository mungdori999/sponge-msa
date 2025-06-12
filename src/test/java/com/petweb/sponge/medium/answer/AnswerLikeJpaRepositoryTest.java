package com.petweb.sponge.medium.answer;

import com.petweb.sponge.post.repository.answer.AnswerLikeEntity;
import com.petweb.sponge.post.repository.answer.AnswerLikeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/answerlike-repository-test-data.sql")
public class AnswerLikeJpaRepositoryTest {


    @Autowired
    private AnswerLikeJpaRepository answerLikeJpaRepository;


    @Test
    public void findByAnswerId는_답변아이디와_유저아이디로_AnswerLikeEntity를_찾는다() {
        // given
        Long answerId = 1L;
        Long userId = 1L;

        // when
        Optional<AnswerLikeEntity> result = answerLikeJpaRepository.findByAnswerId(answerId, userId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getAnswerId()).isEqualTo(answerId);
        assertThat(result.get().getUserId()).isEqualTo(userId);

    }

    @Test
    public void deleteByAnswerId는_훈련사답변과_관련된_모든_답변_좋아요를_삭제한다() {
        // given
        Long answerId = 1L;

        // when
        answerLikeJpaRepository.deleteByAnswerId(answerId);

        // then
        List<AnswerLikeEntity> answerLikeList = answerLikeJpaRepository.findAll();
        assertThat(answerLikeList)
                .noneMatch(answerLike -> answerLike.getAnswerId().equals(answerId));

    }

}
