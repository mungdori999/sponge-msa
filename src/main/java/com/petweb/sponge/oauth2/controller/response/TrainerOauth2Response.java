package com.petweb.sponge.oauth2.controller.response;

import com.petweb.sponge.oauth2.service.LoginOAuth2;
import com.petweb.sponge.trainer.domain.Trainer;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TrainerOauth2Response {

    private Long id;
    private String email;
    private String name;
    private boolean login;
    private String refreshToken;

    public static TrainerOauth2Response register(LoginOAuth2 loginOAuth2,boolean login) {
        return TrainerOauth2Response.builder()
                .email(loginOAuth2.getEmail())
                .name(loginOAuth2.getName())
                .login(login)
                .build();
    }

    public static TrainerOauth2Response login(Trainer trainer, boolean login, String refreshToken) {
        return TrainerOauth2Response.builder()
                .id(trainer.getId())
                .name(trainer.getName())
                .login(login)
                .refreshToken(refreshToken)
                .build();
    }
}
