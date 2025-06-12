package com.petweb.sponge.user.repository;

import com.petweb.sponge.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String email; //로그인 아이디
    private String name; //이름
    private String address;
    private Long createdAt;

    @Builder
    public UserEntity(String email, String name,long createdAt) {
        this.email = email;
        this.name = name;
        this.createdAt=createdAt;
    }

    public User toModel() {
        return User.builder()
                .id(id)
                .email(email)
                .name(name)
                .address(address)
                .createdAt(createdAt)
                .build();
    }

    public static UserEntity from(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.id = user.getId();
        userEntity.email = user.getEmail();
        userEntity.name = user.getName();
        userEntity.address = user.getAddress();
        userEntity.createdAt = user.getCreatedAt();
        return userEntity;
    }
}
