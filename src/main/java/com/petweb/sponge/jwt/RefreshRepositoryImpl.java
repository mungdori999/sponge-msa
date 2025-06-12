package com.petweb.sponge.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RefreshRepositoryImpl implements RefreshRepository {

    private final RefreshRedisRepository refreshRedisRepository;


    @Override
    public void save(String refreshToken) {
        refreshRedisRepository.save(RefreshTokenEntity.from(refreshToken));
    }

    @Override
    public Boolean existsByRefresh(String refreshToken) {
        return refreshRedisRepository.existsById(refreshToken);
    }

    @Override
    public void deleteByRefresh(String refreshToken) {
        refreshRedisRepository.deleteById(refreshToken);
    }
}
