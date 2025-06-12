package com.petweb.sponge.user.controller.response;

import com.petweb.sponge.user.domain.User;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class UserResponse {

    private Long id;
    private String name;
    private String address;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .address(user.getAddress())
                .build();
    }

}
