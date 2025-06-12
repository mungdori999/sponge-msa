package com.petweb.sponge.medium.answer;

import com.petweb.sponge.post.repository.answer.AnswerEntity;
import com.petweb.sponge.post.repository.answer.AnswerJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/answer-repository-test-data.sql")
public class AnswerJpaRepositoryTest {


    @Autowired
    private AnswerJpaRepository answerJpaRepository;
    private final static int PAGE_SIZE = 10;

    @Test
    public void findListByPostId는_postId로_AnswerEntity를_찾는다() {

        // given
        Long postId = 1L;

        // when
        List<AnswerEntity> result = answerJpaRepository.findListByPostId(postId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(answer -> answer.getPostId().equals(postId));
    }

    @Test
    public void findListByTrainerId는_traienrId로_훈련사의_답변을_찾는다() {

        // given
        Long trainerId = 1L;
        int page = 1;
        int offset = page * PAGE_SIZE;

        // when
        List<AnswerEntity> result = answerJpaRepository.findListByTrainerId(trainerId, PAGE_SIZE, offset);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSizeLessThanOrEqualTo(PAGE_SIZE);
        assertThat(result).allMatch(answer -> answer.getTrainerId().equals(trainerId));
    }


}
