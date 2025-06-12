package com.petweb.sponge.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity,Long> {


    @Query("SELECT user FROM UserEntity user where user.email = :email")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("SELECT user from UserEntity user WHERE user.id IN :userIdList")
    List<UserEntity> findByIdList(@Param("userIdList") List<Long> userIdList);
}
