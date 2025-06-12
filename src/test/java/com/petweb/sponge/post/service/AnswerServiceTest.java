package com.petweb.sponge.post.service;

import com.petweb.sponge.post.controller.response.answer.AnswerBasicListResponse;
import com.petweb.sponge.post.controller.response.answer.AnswerCheckResponse;
import com.petweb.sponge.post.controller.response.answer.AnswerDetailsListResponse;
import com.petweb.sponge.post.domain.answer.AdoptAnswer;
import com.petweb.sponge.post.domain.answer.Answer;
import com.petweb.sponge.post.domain.post.*;
import com.petweb.sponge.post.dto.answer.AdoptAnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerUpdate;
import com.petweb.sponge.post.mock.answer.MockAdoptAnswerRepository;
import com.petweb.sponge.post.mock.answer.MockAnswerLikeRepository;
import com.petweb.sponge.post.mock.answer.MockAnswerRepository;
import com.petweb.sponge.post.mock.post.MockPostRepository;
import com.petweb.sponge.post.repository.answer.AdoptAnswerRepository;
import com.petweb.sponge.post.repository.answer.AnswerLikeRepository;
import com.petweb.sponge.post.repository.answer.AnswerRepository;
import com.petweb.sponge.post.repository.post.PostRepository;
import com.petweb.sponge.trainer.domain.History;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.domain.TrainerAddress;
import com.petweb.sponge.trainer.mock.MockTrainerRepository;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.utils.ClockHolder;
import com.petweb.sponge.utils.Gender;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


public class AnswerServiceTest {

    private AnswerService answerService;

    @BeforeEach
    public void init() {
        TrainerRepository trainerRepository = new MockTrainerRepository();
        PostRepository postRepository = new MockPostRepository();
        AnswerRepository answerRepository = new MockAnswerRepository();
        AdoptAnswerRepository adoptAnswerRepository = new MockAdoptAnswerRepository();
        AnswerLikeRepository answerLikeRepository = new MockAnswerLikeRepository();

        ClockHolder clockHolder = new TestClockHolder(12345L);
        answerService = AnswerService.builder()
                .trainerRepository(trainerRepository)
                .postRepository(postRepository)
                .answerRepository(answerRepository)
                .adoptAnswerRepository(adoptAnswerRepository)
                .answerLikeRepository(answerLikeRepository)
                .clockHolder(clockHolder)
                .build();
        for (int i = 1; i <= 15; i++) {
            postRepository.save(Post.builder()
                    .postContent(PostContent.builder()
                            .title("테스트 제목")
                            .content("테스트 게시글 내용")
                            .duration("한달이상")
                            .createdAt(0L)
                            .modifiedAt(0L)
                            .build())
                    .userId(1L)
                    .petId(1L)
                    .answerCount(i <= 13 ? 2 : 0)
                    .postFileList(List.of(
                            PostFile.builder().fileUrl("").build(),
                            PostFile.builder().fileUrl("").build()
                    ))
                    .tagList(List.of(
                            Tag.builder().hashtag("짖음").build(),
                            Tag.builder().hashtag("건강").build()
                    ))
                    .postCategoryList(List.of(
                            PostCategory.builder().categoryCode(100L).build(),
                            PostCategory.builder().categoryCode(200L).build()
                    ))
                    .build());
        }


        Trainer trainer1 = trainerRepository.save(Trainer.builder()
                .email("test1@naver.com")
                .name("test1")
                .gender(Gender.MALE.getCode())
                .profileImgUrl("")
                .content("안녕")
                .years(2)
                .createdAt(0L)
                .adoptCount(7)
                .trainerAddressList(List.of(
                        TrainerAddress.builder()
                                .city("서울")
                                .town("강남구")
                                .build()
                ))
                .historyList(List.of(History.builder()
                        .title("프로젝트 A")
                        .startDt("202401")
                        .endDt("202406")
                        .description("Spring Boot 기반 웹 애플리케이션 개발")
                        .build()))
                .build());

        Trainer trainer2 = trainerRepository.save(Trainer.builder()
                .email("test2@naver.com")
                .name("test2")
                .gender(Gender.MALE.getCode())
                .profileImgUrl("")
                .content("안녕")
                .years(2)
                .createdAt(0L)
                .adoptCount(7)
                .trainerAddressList(List.of(
                        TrainerAddress.builder()
                                .city("서울")
                                .town("강남구")
                                .build()
                ))
                .historyList(List.of(History.builder()
                        .title("프로젝트 A")
                        .startDt("202401")
                        .endDt("202406")
                        .description("Spring Boot 기반 웹 애플리케이션 개발")
                        .build()))
                .build());
        for (int i = 1; i <= 13; i++) {
            answerRepository.save(Answer.builder()
                    .content("테스트 내용입니다")
                    .createdAt(0L)
                    .modifiedAt(0L)
                    .postId((long) i)
                    .trainerId(trainer1.getId())
                    .build());
        }
        for (int i = 1; i <= 13; i++) {
            answerRepository.save(Answer.builder()
                    .content("테스트2 내용입니다")
                    .createdAt(0L)
                    .modifiedAt(0L)
                    .postId((long) i)
                    .trainerId(trainer2.getId())
                    .build());
        }
        for (int i = 1; i <= 13; i++) {
            if (i % 2 == 0) { // i가 짝수일 때만 실행
                adoptAnswerRepository.save(AdoptAnswer.builder()
                        .trainerId(trainer1.getId())
                        .answerId((long) i)
                        .userId(1L)
                        .build());
            }
        }

        for (int i = 1; i <= 13; i++) {
            if (i % 2 == 0) { // i가 짝수일 때만 실행
                adoptAnswerRepository.save(AdoptAnswer.builder()
                        .trainerId(trainer2.getId())
                        .answerId((long) i)
                        .userId(1L)
                        .build());
            }
        }

    }

    @Test
    public void findAnswerList는_ANSWER들과_TRAINER를_조회한다() {
        // given
        Long postId = 1L;

        // when
        List<AnswerDetailsListResponse> result = answerService.findAnswerList(postId);

        // then
        assertThat(result.get(0).getAnswerResponse().getContent()).isEqualTo("테스트 내용입니다");
        assertThat(result.get(0).getTrainerShortResponse().getName()).isEqualTo("test1");
        assertThat(result.get(0).getAnswerResponse().getCreatedAt()).isEqualTo(0L);
        assertThat(result.get(0).isCheckAdopt()).isFalse();
        assertThat(result.get(1).isCheckAdopt()).isFalse();
        assertThat(result.get(1).getTrainerShortResponse().getName()).isEqualTo("test2");
        assertThat(result).hasSize(2);
    }

    @Test
    public void findAnswerListInfo는_ANSWER들을_조회한다() {
        // given
        Long trainerId = 1L;
        int page = 0;

        // when
        List<AnswerBasicListResponse> result = answerService.findAnswerListInfo(trainerId, page);

        // then
        assertThat(result.get(0).getAnswerResponse().getContent()).isEqualTo("테스트 내용입니다");
        assertThat(result.get(0).getAnswerResponse().getCreatedAt()).isEqualTo(0L);
        assertThat(result).hasSize(10);

    }

    @Test
    public void create는_ANSWER를_저장한다() {
        // given
        Long trainerId = 1L;
        AnswerCreate answerCreate = AnswerCreate.builder()
                .postId(15L)
                .content("새로운 내용입니다")
                .build();

        // when
        Answer result = answerService.create(trainerId, answerCreate);

        // then
        assertThat(result.getId()).isEqualTo(27L);
        assertThat(result.getPostId()).isEqualTo(15L);
        assertThat(result.getContent()).isEqualTo("새로운 내용입니다");
    }

    @Test
    public void update는_ANSWER를_수정한다() {
        // given
        Long answerId = 1L;
        Long trainerId = 1L;
        AnswerUpdate answerUpdate = AnswerUpdate.builder()
                .content("수정 내용입니다").build();

        // when
        answerService.update(answerId, answerUpdate, trainerId);

        // then
        List<AnswerDetailsListResponse> answerList = answerService.findAnswerList(1L);

        assertThat(answerList.get(1).getAnswerResponse().getContent()).isEqualTo("수정 내용입니다");
    }

    @Test
    public void delete는_ANSWER를_삭제한다() {
        // given
        Long trainerId = 1L;
        Long answerId = 1L;

        // when
        answerService.delete(trainerId, answerId);

        // then
        List<AnswerDetailsListResponse> answerList = answerService.findAnswerList(1L);
        assertThat(answerList).hasSize(1);
    }

    @Test
    public void createAdoptAnswer는_ADOPTANSWER를_저장한다() {
        // given
        Long userId = 1L;
        AdoptAnswerCreate answerCreate = AdoptAnswerCreate.builder()
                .answerId(1L)
                .postId(1L)
                .trainerId(1L)
                .build();


        // when
        AdoptAnswer result = answerService.createAdoptAnswer(userId, answerCreate);

        // then
        assertThat(result.getAnswerId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getTrainerId()).isEqualTo(1L);
    }

    @Test
    public void findCheck는_내가_이답변을_추천했는지를조회한다() {
        // given
        Long userId = 1L;
        Long answerId = 1L;

        // when
        AnswerCheckResponse result = answerService.findCheck(userId, answerId);

        // then
        assertThat(result.isLikeCheck()).isFalse();
    }

    @Test
    public void updateLike는_추천수를_업데이트한다() {
        // given
        Long userId = 1L;
        Long answerId = 1L;

        // when
        answerService.updateLike(userId, answerId);

        // then
        AnswerCheckResponse check = answerService.findCheck(userId, answerId);
        assertThat(check.isLikeCheck()).isTrue();
    }


}
