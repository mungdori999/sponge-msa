package com.petweb.sponge.utils;

import lombok.Getter;

@Getter
public enum ResponseHttpStatus {

    EXPIRE_ACCESS_TOKEN(4000),
    EXPIRE_REFRESH_TOKEN(4100);

    private final int code;

    ResponseHttpStatus(int code) {
        this.code = code;
    }
}
