package com.petweb.sponge.exception;

import lombok.Getter;

@Getter
public enum TokenHttpStatus {

    EXPIRE_ACCESS_TOKEN(4100,"액세스 토큰이 만료되었습니다."),
    NOT_FOUND_ACCESS_TOKEN(4150,"엑세스 토큰이 없습니다."),
    EXPIRE_REFRESH_TOKEN(4200,"리프레시 토큰이 만료되었습니다."),
    NOT_FOUND_REFRESH_TOKEN(4250,"리프레시 토큰이 없습니다.");

    private final int code;
    private final String message;

    TokenHttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
