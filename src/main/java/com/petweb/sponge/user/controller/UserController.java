package com.petweb.sponge.user.controller;

import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.RefreshToken;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.user.controller.response.UserResponse;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.dto.UserUpdate;
import com.petweb.sponge.user.service.UserService;
import com.petweb.sponge.utils.AuthorizationUtil;
import com.petweb.sponge.utils.LoginType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Logging
public class UserController {
    private final UserService userService;
    private final AuthorizationUtil authorizationUtil;
    private final JwtUtil jwtUtil;

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
     * 유저 정보 수정
     *
     * @param id
     * @return
     */
    @PatchMapping("/{id}")
    @UserAuth
    public ResponseEntity<RefreshToken> update(@PathVariable("id") Long id, @RequestBody UserUpdate userUpdate) {
        if (authorizationUtil.getLoginId().equals(id)) {
            User user = userService.update(id, userUpdate);
            Token token = jwtUtil.createToken(user.getId(), user.getName(), LoginType.USER.getLoginType());
            return ResponseEntity.ok().header("Authorization", token.getAccessToken())
                    .body(new RefreshToken(token.getRefreshToken()));
        } else {
            throw new LoginIdError();
        }
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
