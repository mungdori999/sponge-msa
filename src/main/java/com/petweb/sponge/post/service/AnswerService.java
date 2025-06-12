package com.petweb.sponge.post.service;

import com.petweb.sponge.exception.error.*;
import com.petweb.sponge.post.controller.response.answer.*;
import com.petweb.sponge.post.domain.answer.AdoptAnswer;
import com.petweb.sponge.post.domain.answer.Answer;
import com.petweb.sponge.post.domain.answer.AnswerLike;
import com.petweb.sponge.post.domain.post.Post;
import com.petweb.sponge.post.dto.answer.AdoptAnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerUpdate;
import com.petweb.sponge.post.repository.answer.AdoptAnswerRepository;
import com.petweb.sponge.post.repository.answer.AnswerLikeRepository;
import com.petweb.sponge.post.repository.answer.AnswerRepository;
import com.petweb.sponge.post.repository.post.PostRepository;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class AnswerService {

    private final TrainerRepository trainerRepository;
    private final PostRepository postRepository;
    private final AnswerRepository answerRepository;
    private final AdoptAnswerRepository adoptAnswerRepository;
    private final AnswerLikeRepository answerLikeRepository;
    private final ClockHolder clockHolder;

    /**
     * 훈련사 답변 조회
     *
     * @param postId
     * @return
     */
    @Transactional(readOnly = true)
    public List<AnswerDetailsListResponse> findAnswerList(Long postId) {
        List<Answer> answerList = answerRepository.findListByPostId(postId);
        List<Long> trainerIdList = answerList.stream().map((Answer::getTrainerId)).collect(Collectors.toList());
        List<Trainer> trainerList = trainerRepository.findShortByIdList(trainerIdList);
        List<Long> answerIdList = answerList.stream().map(Answer::getId).collect(Collectors.toList());
        List<AdoptAnswer> adoptAnswerList = adoptAnswerRepository.findListByAnswerIdList(answerIdList);

        Map<Long, Trainer> trainerMap = trainerList.stream()
                .collect(Collectors.toMap(Trainer::getId, Function.identity()));
        return answerList.stream()
                .map(answer -> {
                    Trainer trainer = trainerMap.get(answer.getTrainerId());
                    boolean adoptCheck = adoptAnswerList.stream()
                            .anyMatch(adoptAnswerPresent ->
                                    Objects.equals(adoptAnswerPresent.getTrainerId(), trainer.getId())
                            );
                    return AnswerDetailsListResponse.from(AnswerResponse.from(answer), TrainerShortResponse.from(trainer), adoptCheck);
                })
                .collect(Collectors.toList());

    }

    /**
     * 아이디로 답변 조회
     *
     * @param trainerId
     * @param page
     */
    @Transactional(readOnly = true)
    public List<AnswerBasicListResponse> findAnswerListInfo(Long trainerId, int page) {
        List<Answer> answerList = answerRepository.findListByTrainerId(trainerId, page);
        List<AdoptAnswer> adoptAnswerList = adoptAnswerRepository.findListByTrainerId(trainerId);
        Set<Long> adoptAnswerIds = adoptAnswerList.stream()
                .map(AdoptAnswer::getAnswerId)
                .collect(Collectors.toSet());

        return answerList.stream().map(answer -> {
            boolean isAdopted = adoptAnswerIds.contains(answer.getId());
            return AnswerBasicListResponse.from(AnswerResponse.from(answer), isAdopted);
        }).collect(Collectors.toList());
    }


    /**
     * 훈련사 답변 저장
     *
     * @param loginId
     * @param answerCreate
     */
    @Transactional
    public Answer create(Long loginId, AnswerCreate answerCreate) {
        Post post = postRepository.findById(answerCreate.getPostId()).orElseThrow(
                NotFoundPost::new);
        Trainer trainer = trainerRepository.findById(loginId).orElseThrow(
                NotFoundTrainer::new);
        Answer answer = Answer.from(trainer.getId(), answerCreate, clockHolder);
        answer = answerRepository.save(answer);

        post.increaseAnswerCount();
        postRepository.save(post);
        return answer;
    }

    /**
     * 훈련사 답변내용 수정
     *
     * @param id
     * @param answerUpdate
     * @param loginId
     */
    @Transactional
    public void update(Long id, AnswerUpdate answerUpdate, Long loginId) {
        Answer answer = answerRepository.findById(id).orElseThrow(
                NotFoundAnswer::new);
        answer.checkTrainer(loginId);
        answer = answer.update(answerUpdate, clockHolder);
        answerRepository.save(answer);
    }

    /**
     * 훈련사 답변 삭제
     *
     * @param loginId
     * @param id
     */
    @Transactional
    public void delete(Long loginId, Long id) {
        Answer answer = answerRepository.findById(id).orElseThrow(
                NotFoundAnswer::new);
        answer.checkTrainer(loginId);
        Optional<AdoptAnswer> adoptAnswer = adoptAnswerRepository.findByAnswerId(answer.getId());
        Post post = postRepository.findShortById(answer.getPostId()).orElseThrow(
                NotFoundPost::new);

        //게시글 훈련사 답변 감소
        post.decreaseAnswerCount();
        postRepository.save(post);

        //채택되어있다면 카운트 감소
        if (adoptAnswer.isPresent()) {
            Trainer trainer = trainerRepository.findShortById(adoptAnswer.get().getTrainerId()).orElseThrow(
                    NotFoundTrainer::new);
            adoptAnswerRepository.delete(adoptAnswer.get());
            trainer.decreaseAdoptCount();
            trainerRepository.save(trainer);
        }
        // 좋아요 전체 삭제
        answerLikeRepository.deleteByAnswerId(answer.getId());
        answerRepository.delete(answer);
    }

    /**
     * 답변 채택 저장
     *
     * @param adoptAnswerCreate
     * @param loginId
     */
    @Transactional
    public AdoptAnswer createAdoptAnswer(Long loginId, AdoptAnswerCreate adoptAnswerCreate) {
        AdoptAnswer adoptAnswer = AdoptAnswer.from(loginId, adoptAnswerCreate);
        Trainer trainer = trainerRepository.findShortById(adoptAnswer.getTrainerId()).orElseThrow(NotFoundTrainer::new);
        Post post = postRepository.findShortById(adoptAnswerCreate.getPostId()).orElseThrow(
                NotFoundPost::new);
        post.checkUser(loginId);
        trainer.increaseAdoptCount();
        trainerRepository.save(trainer);
        return adoptAnswerRepository.save(adoptAnswer);
    }

    /**
     * 훈련사 답변 추천 조회
     *
     * @param loginId
     * @param answerId
     * @return
     */
    @Transactional(readOnly = true)
    public AnswerCheckResponse findCheck(Long loginId, Long answerId) {
        Optional<AnswerLike> answerLike = answerLikeRepository.findByAnswerId(answerId, loginId);
        return AnswerCheckResponse.from(answerLike);
    }

    /**
     * 훈련사 답변 추천
     *
     * @param answerId
     * @param loginId
     */
    @Transactional
    public void updateLike(Long loginId, Long answerId) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(NotFoundAnswer::new);
        Optional<AnswerLike> answerLike = answerLikeRepository.findByAnswerId(answerId, loginId);


        /**
         * 추천이 이미 있다면 추천을 삭제 추천수 -1
         * 추천이 없다면 추천을 저장 추천수 +1
         */
        if (answerLike.isPresent()) {
            answer.decreaseLikeCount();
            answerLikeRepository.delete(answerLike.get());
            answerRepository.save(answer);
        } else {
            AnswerLike newAnswerLike = AnswerLike.from(answerId, loginId);
            answer.increaseLikeCount();
            answerLikeRepository.save(newAnswerLike);
            answerRepository.save(answer);
        }
    }


}
