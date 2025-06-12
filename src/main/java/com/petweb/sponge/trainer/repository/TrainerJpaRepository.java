package com.petweb.sponge.trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainerJpaRepository extends JpaRepository<TrainerEntity,Long>,TrainerQueryDslRepository {

    @Query("SELECT trainer FROM TrainerEntity trainer where trainer.email = :email")
    Optional<TrainerEntity> findByEmail(@Param("email") String email);

    @Query("SELECT trainer FROM TrainerEntity trainer WHERE trainer.id IN :trainerIdList")
    List<TrainerEntity> findShortByIdList(@Param("trainerIdList") List<Long> trainerIdList);
}
