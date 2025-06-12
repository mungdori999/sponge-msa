package com.petweb.sponge.jwt;


import org.springframework.transaction.annotation.Transactional;

public interface RefreshRepository {

    @Transactional
    void save(String refreshToken);
    @Transactional(readOnly = true)
    Boolean existsByRefresh(String refreshToken);
    @Transactional
    void deleteByRefresh(String refreshToken);
}
