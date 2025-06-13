package com.petweb.sponge.oauth2.controller;

import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.RefreshRepository;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.oauth2.controller.response.TrainerOauth2Response;
import com.petweb.sponge.oauth2.controller.response.UserOauth2Response;
import com.petweb.sponge.oauth2.dto.LoginRequest;
import com.petweb.sponge.oauth2.service.KaKaoService;
import com.petweb.sponge.oauth2.service.OAuth2Service;
import com.petweb.sponge.oauth2.service.LoginOAuth2;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class OAuth2Controller {

    private final OAuth2Service oAuth2Service;
    private final KaKaoService kaKaoService;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;


    @PostMapping("/kakao/user")
    public ResponseEntity<UserOauth2Response> authUserKaKao(@RequestBody LoginRequest loginRequest) {
        LoginOAuth2 loginOAuth2 = kaKaoService.getKaKaoInfo(loginRequest.getAccessToken());
        User user = oAuth2Service.loadUser(loginOAuth2);
        Token token = jwtUtil.createToken(user.getId(), user.getName(), loginRequest.getLoginType());
        refreshRepository.save(token.getRefreshToken());
        return ResponseEntity.ok().header("Authorization", token.getAccessToken())
                .body(UserOauth2Response.from(user, token.getRefreshToken()));
    }

    @PostMapping("/kakao/trainer")
    public ResponseEntity<TrainerOauth2Response> authTrainerKaKao(@RequestBody LoginRequest loginRequest) {
        LoginOAuth2 loginOAuth2 = kaKaoService.getKaKaoInfo(loginRequest.getAccessToken());
        Trainer trainer = oAuth2Service.checkTrainer(loginOAuth2);
        if (trainer == null) {
            return ResponseEntity.ok().body(TrainerOauth2Response.register(loginOAuth2, false));
        } else {
            Token token = jwtUtil.createToken(trainer.getId(), trainer.getName(), loginRequest.getLoginType());
            refreshRepository.save(token.getRefreshToken());
            return ResponseEntity.ok().header("Authorization", token.getAccessToken())
                    .body(TrainerOauth2Response.login(trainer, true, token.getRefreshToken()));
        }
    }
}
