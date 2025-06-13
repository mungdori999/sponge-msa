package com.petweb.sponge.exception;

import lombok.Getter;

@Getter
public class ResponseError {

    private int code;
    private String message;

    public ResponseError(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
