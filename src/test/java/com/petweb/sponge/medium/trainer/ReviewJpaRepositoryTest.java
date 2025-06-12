package com.petweb.sponge.medium.trainer;

import com.petweb.sponge.trainer.repository.ReviewEntity;
import com.petweb.sponge.trainer.repository.ReviewJpaRepository;
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
@Sql("/sql/repository/review-repository-test-data.sql")
public class ReviewJpaRepositoryTest {

    @Autowired
    private ReviewJpaRepository reviewJpaRepository;
    private final static int PAGE_SIZE = 10;


    @Test
    public void findByUserId는_userId로_리뷰를_찾는다() {

        // given
        Long userId = 1L;
        Long trainerId = 1L;

        // when
        Optional<ReviewEntity> result = reviewJpaRepository.findByUserId(userId, trainerId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getUserId()).isEqualTo(userId);
        assertThat(result.get().getTrainerId()).isEqualTo(trainerId);
        assertThat(result.get().getContent()).isNotNull();
        assertThat(result.get().getScore()).isBetween(1, 5);
    }


    @Test
    public void findListByTrainerId는_trainerId로_리뷰여러개를_찾는다() {

        // given
        Long trainerId = 1L;
        int page = 0;
        int offset = page * PAGE_SIZE;

        // when
        List<ReviewEntity> result = reviewJpaRepository.findListByTrainerId(trainerId, PAGE_SIZE, offset);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).hasSizeLessThanOrEqualTo(PAGE_SIZE);
        assertThat(result).allMatch(review -> review.getTrainerId().equals(trainerId));

    }

}
