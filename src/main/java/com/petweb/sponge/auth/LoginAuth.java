package com.petweb.sponge.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginAuth {
    private Long id;
    private String loginType;
}
