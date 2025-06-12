package com.petweb.sponge.jwt;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Random;
import java.util.UUID;

@Getter
@RedisHash(value = "refreshToken", timeToLive = 2592000)
public class RefreshTokenEntity {

    @Id
    private String refreshToken;
    private Long id;

    @Builder
    public RefreshTokenEntity(String refreshToken, Long id) {
        this.refreshToken = refreshToken;
        this.id = id;
    }

    static public RefreshTokenEntity from(String refreshToken) {
        return RefreshTokenEntity.builder()
                .refreshToken(refreshToken)
                .id(UUID.randomUUID().getMostSignificantBits())
                .build();
    }
}
