package com.petweb.sponge.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreate {

    private String email;
    private String name;
}
