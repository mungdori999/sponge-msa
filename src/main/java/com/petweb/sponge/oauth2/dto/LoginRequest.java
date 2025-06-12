package com.petweb.sponge.oauth2.dto;

import lombok.Getter;

@Getter
public class LoginRequest {

    private String accessToken;
    private String loginType;
}
