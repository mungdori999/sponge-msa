package com.petweb.sponge.user.controller;

import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.user.controller.response.UserResponse;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.dto.UserCreate;
import com.petweb.sponge.user.dto.UserUpdate;
import com.petweb.sponge.user.service.UserService;
import com.petweb.sponge.utils.AuthorizationUtil;
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
@RequestMapping("/api/user")
@Logging
public class UserController {
    private final UserService userService;
    private final AuthorizationUtil authorizationUtil;

    /**
     * 유저 단건조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getById(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return new ResponseEntity<>(UserResponse.from(user), HttpStatus.OK);
    }

    /**
     * email로 유저 단건조회
     *
     * @param email
     * @return
     */
    @GetMapping()
    public ResponseEntity<UserResponse> getByEmail(@RequestParam("email") String email) {
        User user = userService.getByEmail(email);
        return new ResponseEntity<>(UserResponse.from(user), HttpStatus.OK);
    }

    /**
     * user 전체조회
     *
     * @param idList
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity<List<UserResponse>> getListByIdList(@RequestParam("idList") List<Long> idList) {
        List<User> userList = userService.getListByIdList(idList);
        return new ResponseEntity<>(userList.stream().map(UserResponse::from).collect(Collectors.toList())
                , HttpStatus.OK);
    }

    /**
     * 자신의 계정정보를 불러옴
     *
     * @return
     */
    @GetMapping("/my_info")
    @UserAuth
    public ResponseEntity<UserResponse> getMyInfo() {
        User user = userService.findMyInfo(authorizationUtil.getLoginId());
        return new ResponseEntity<>(UserResponse.from(user), HttpStatus.OK);
    }

    /**
     * 유저 회원가입
     *
     * @param userCreate
     * @return
     */
    @PostMapping
    public ResponseEntity<UserResponse> save(@RequestBody UserCreate userCreate) {
        User user = userService.save(userCreate);
        return new ResponseEntity<>(UserResponse.from(user), HttpStatus.OK);
    }

    /**
     * 유저 정보 수정
     *
     * @param id
     * @return
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") Long id, @RequestBody UserUpdate userUpdate) {
        User user = userService.update(id, userUpdate);
        return new ResponseEntity<>(UserResponse.from(user), HttpStatus.OK);
    }


    /**
     * 회원탈퇴
     *
     * @param id
     * @param response
     */
    @DeleteMapping("/{id}")
    @UserAuth
    public void delete(@PathVariable("id") Long id, HttpServletResponse response) {
        if (authorizationUtil.getLoginId().equals(id)) {
            userService.delete(id);
        } else {
            throw new LoginIdError();
        }
        Cookie cookie = new Cookie("Authorization", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setStatus(200);
    }


}
