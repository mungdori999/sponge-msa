package com.petweb.sponge.post.controller;

import com.petweb.sponge.auth.TrainerAuth;
import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.post.controller.response.answer.AnswerCheckResponse;
import com.petweb.sponge.post.controller.response.answer.AnswerBasicListResponse;
import com.petweb.sponge.post.controller.response.answer.AnswerDetailsListResponse;
import com.petweb.sponge.post.dto.answer.*;
import com.petweb.sponge.post.service.AnswerService;
import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/answer")
@Logging
public class AnswerController {

    private final AnswerService answerService;
    private final AuthorizationUtil authorizationUtil;


    /**
     * 문제행동글에 딸린 훈련사 답변 조회
     *
     * @param postId
     * @return
     */
    @GetMapping
    public ResponseEntity<List<AnswerDetailsListResponse>> getAllAnswer(@RequestParam Long postId) {
        List<AnswerDetailsListResponse> answerList = answerService.findAnswerList(postId);
        return new ResponseEntity<>(answerList, HttpStatus.OK);
    }

    /**
     * 훈련사 답변 조회
     * @param trainerId
     * @param page
     * @return
     */
    @GetMapping("/trainer")
    public ResponseEntity<List<AnswerBasicListResponse>> getListByTrainerId(@RequestParam("trainerId") Long trainerId, @RequestParam("page") int page) {
        List<AnswerBasicListResponse> answerList = answerService.findAnswerListInfo(trainerId, page);
        return new ResponseEntity<>(answerList, HttpStatus.OK);
    }

    /**
     * 내가쓴 답변 조회
     *
     * @param page
     */
    @GetMapping("/my_info")
    @TrainerAuth
    public ResponseEntity<List<AnswerBasicListResponse>> getMyAnswer(@RequestParam("page") int page) {
        List<AnswerBasicListResponse> answerList = answerService.findAnswerListInfo(authorizationUtil.getLoginId(), page);
        return new ResponseEntity<>(answerList, HttpStatus.OK);
    }

    /**
     * 훈련사 답변 작성
     *
     * @param answerCreate
     */
    @PostMapping
    @TrainerAuth
    public void create(@RequestBody AnswerCreate answerCreate) {
        answerService.create(authorizationUtil.getLoginId(), answerCreate);
    }

    /**
     * 훈련사 답변 수정
     *
     * @param id
     * @param answerUpdate
     */
    @PatchMapping("/{id}")
    @TrainerAuth
    public void update(@PathVariable Long id, @RequestBody AnswerUpdate answerUpdate) {
        answerService.update(id, answerUpdate, authorizationUtil.getLoginId());
    }

    /**
     * 훈련사 답변 삭제
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    @TrainerAuth
    public void delete(@PathVariable Long id) {
        answerService.delete(authorizationUtil.getLoginId(), id);
    }

    /**
     * 훈련사 답변 채택
     *
     * @param adoptAnswerCreate
     */
    @PostMapping("/adopt")
    @UserAuth
    public void createAdoptAnswer(@RequestBody AdoptAnswerCreate adoptAnswerCreate) {
        answerService.createAdoptAnswer(authorizationUtil.getLoginId(), adoptAnswerCreate);
    }

    /**
     * 훈련사 답변 추천 조회
     *
     * @param answerId
     * @return
     */
    @GetMapping("/check")
    @UserAuth
    public ResponseEntity<AnswerCheckResponse> getMyCheck(@RequestParam("answerId") Long answerId) {
        AnswerCheckResponse answerCheckResponse = answerService.findCheck(authorizationUtil.getLoginId(), answerId);
        return new ResponseEntity<>(answerCheckResponse, HttpStatus.OK);
    }

    /**
     * 훈련사 답변 추천수 업데이트
     *
     * @param answerId
     */
    @PostMapping("/like")
    @UserAuth
    public void updateLike(@RequestParam("answerId") Long answerId) {
        answerService.updateLike(authorizationUtil.getLoginId(), answerId);
    }


}
