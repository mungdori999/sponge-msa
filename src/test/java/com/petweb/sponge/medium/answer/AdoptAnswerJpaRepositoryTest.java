package com.petweb.sponge.medium.answer;

import com.petweb.sponge.post.repository.answer.AdoptAnswerEntity;
import com.petweb.sponge.post.repository.answer.AdoptAnswerJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/adoptanswer-repository-test-data.sql")
public class AdoptAnswerJpaRepositoryTest {


    @Autowired
    private AdoptAnswerJpaRepository adoptAnswerJpaRepository;


    @Test
    public void findListByTrainerId는_traienrId로_AdoptAnswer을_찾는다() {

        // given
        Long trainerId = 1L;

        // when

        List<AdoptAnswerEntity> result = adoptAnswerJpaRepository.findListByTrainerId(trainerId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).allMatch(adoptAnswer -> adoptAnswer.getTrainerId().equals(trainerId));

    }

    @Test
    public void findListByAnswerIdList는_여러개_answerId로_모든_AdoptAnswer를_찾는다() {

        // given
        List<Long> answerIdList = new ArrayList<>(List.of(1L,2L,3L));

        // when
        List<AdoptAnswerEntity> result = adoptAnswerJpaRepository.findListByAnswerIdList(answerIdList);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(3);
        assertThat(result).allMatch(adoptAnswer -> answerIdList.contains(adoptAnswer.getAnswerId()));
    }



}
