package com.petweb.sponge.trainer.controller;

import com.petweb.sponge.auth.TrainerAuth;
import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.trainer.controller.response.TrainerResponse;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.dto.TrainerCreate;
import com.petweb.sponge.trainer.dto.TrainerUpdate;
import com.petweb.sponge.trainer.service.TrainerService;
import com.petweb.sponge.utils.AuthorizationUtil;
import com.petweb.sponge.utils.LoginType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainer")
@Logging
public class TrainerController {

    private final TrainerService trainerService;
    private final AuthorizationUtil authorizationUtil;

    /**
     * 훈련사 단건조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<TrainerResponse> getById(@PathVariable("id") Long id) {
        Trainer trainer = trainerService.getById(id);
        return new ResponseEntity<>(TrainerResponse.from(trainer), HttpStatus.OK);
    }

    /**
     * 이메일로 조회
     *
     * @param email
     * @return
     */
    @GetMapping()
    public ResponseEntity<TrainerResponse> getByEmail(@RequestParam("email") String email) {
        Trainer trainer = trainerService.getByEmail(email);
        return new ResponseEntity<>(TrainerResponse.from(trainer), HttpStatus.OK);
    }

    /**
     * trainer 전체조회
     *
     * @param idList
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<TrainerResponse>> getListByIdList(@RequestParam("idList") List<Long> idList) {
        List<Trainer> trainerList = trainerService.getListByIdList(idList);
        return new ResponseEntity<>(trainerList.stream().map(TrainerResponse::from).collect(Collectors.toList())
                , HttpStatus.OK);
    }

    /**
     * 자신의 계정정보를 가져옴
     *
     * @return
     */
    @GetMapping("/my_info")
    @TrainerAuth
    public ResponseEntity<TrainerResponse> getMyInfo() {
        Trainer trainer = trainerService.findMyInfo(authorizationUtil.getLoginId());
        return new ResponseEntity<>(TrainerResponse.from(trainer), HttpStatus.OK);
    }

    /**
     * 훈련사 정보 저장
     *
     * @param trainerCreate
     * @return
     */
    @PostMapping()
    public ResponseEntity<TrainerResponse> create(@RequestBody TrainerCreate trainerCreate) {
        Trainer trainer = trainerService.create(trainerCreate);
        return new ResponseEntity<>(TrainerResponse.from(trainer), HttpStatus.OK);
    }

    /**
     * 훈련사 정보 수정
     *
     * @param id
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TrainerResponse> update(@PathVariable("id") Long id, @RequestBody TrainerUpdate trainerUpdate) {
        Trainer trainer = trainerService.update(id, trainerUpdate);
        return new ResponseEntity<>(TrainerResponse.from(trainer), HttpStatus.OK);
    }

    /**
     * 회원탈퇴
     *
     * @param trainerId
     */
    @DeleteMapping("/{trainerId}")
    @TrainerAuth
    public void removeTrainer(@PathVariable("trainerId") Long trainerId, HttpServletResponse response) {
        if (authorizationUtil.getLoginId().equals(trainerId)) {
            trainerService.deleteTrainer(trainerId);
        } else {
            throw new LoginIdError();
        }

        //로그인 쿠키 삭제
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setStatus(200);
    }
}
