package com.petweb.sponge.user.domain;

import com.petweb.sponge.user.dto.UserUpdate;
import lombok.Builder;
import lombok.Getter;


@Getter
public class User {

    private Long id;
    private String email; //로그인 아이디
    private String name; //이름
    private String address; // 주소
    private Long createdAt;

    @Builder
    public User(Long id, String email, String name, String address, long createdAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.address = address;
        this.createdAt = createdAt;
    }

    public User update(UserUpdate userUpdate) {
        return User.builder()
                .id(id)
                .email(email)
                .name(userUpdate.getName())
                .address(userUpdate.getAddress())
                .createdAt(createdAt).build();
    }
}
