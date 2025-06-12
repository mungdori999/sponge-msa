package com.petweb.sponge.trainer.service;

import com.petweb.sponge.trainer.controller.response.ReviewCheckResponse;
import com.petweb.sponge.trainer.controller.response.ReviewResponse;
import com.petweb.sponge.trainer.domain.History;
import com.petweb.sponge.trainer.domain.Review;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.domain.TrainerAddress;
import com.petweb.sponge.trainer.dto.ReviewCreate;
import com.petweb.sponge.trainer.mock.MockReviewRepository;
import com.petweb.sponge.trainer.mock.MockTrainerRepository;
import com.petweb.sponge.trainer.repository.ReviewRepository;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.mock.MockUserRepository;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.ClockHolder;
import com.petweb.sponge.utils.Gender;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ReviewServiceTest {

    private ReviewService reviewService;

    @BeforeEach
    public void init() {
        UserRepository userRepository = new MockUserRepository();
        TrainerRepository trainerRepository = new MockTrainerRepository();
        ReviewRepository reviewRepository = new MockReviewRepository();
        ClockHolder clockHolder = new TestClockHolder(12345L);
        User user = userRepository.save(User.builder()
                .email("abc@test.com")
                .name("테스트")
                .createdAt(0L).build());
        Trainer trainer = trainerRepository.save(Trainer.builder()
                .email("test1@naver.com")
                .name("test1")
                .gender(Gender.MALE.getCode())
                .profileImgUrl("")
                .content("안녕")
                .years(2)
                .createdAt(0L)
                .trainerAddressList(List.of(
                        TrainerAddress.builder()
                                .city("서울")
                                .town("강남구")
                                .build()

                ))
                .historyList(List.of(History.builder()
                        .title("프로젝트 B")
                        .startDt("202307")
                        .endDt("202312")
                        .description("Flutter 기반 모바일 앱 개발")
                        .build()))
                .build());
        reviewService = ReviewService.builder()
                .userRepository(userRepository)
                .trainerRepository(trainerRepository)
                .reviewRepository(reviewRepository)
                .clockHolder(clockHolder)
                .build();

        Review review = Review.builder()
                .score(5)
                .content("훌륭한 트레이너입니다!")
                .createdAt(0L)
                .trainerId(trainer.getId())
                .userId(user.getId())
                .createdAt(0L)
                .build();
        reviewRepository.save(review);
    }


    @Test
    public void findCheck는_REVIEW를_작성했는지_체크한다() {
        // given
        Long userId = 1L;
        Long trainerId = 1L;

        // when
        ReviewCheckResponse result = reviewService.findCheck(userId, trainerId);

        // then
        assertThat(result.isReviewCheck()).isTrue();

    }

    @Test
    public void getListByTrainerId는_REVIEW를_조회한다() {
        // given
        Long trainerId = 1L;
        int page = 0;

        // when
        List<ReviewResponse> result = reviewService.getListByTrainerId(trainerId, page);

        // then
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getScore()).isEqualTo(5);
    }

    @Test
    public void create는_REVIEW를_저장한다() {
        // given
        Long userId = 1L;
        ReviewCreate reviewCreate = ReviewCreate.builder()
                .trainerId(1L)
                .score(3)
                .content("별로에요").build();

        // when
        Review result = reviewService.create(userId, reviewCreate);

        // then
        assertThat(result.getTrainerId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("별로에요");

    }
}
