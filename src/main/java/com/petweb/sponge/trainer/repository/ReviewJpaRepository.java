package com.petweb.sponge.trainer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewJpaRepository extends JpaRepository<ReviewEntity, Long> {
    @Query("SELECT r FROM ReviewEntity r WHERE r.userId = :loginId AND r.trainerId=:trainerId")
    Optional<ReviewEntity> findByUserId(@Param("loginId") Long loginId, @Param("trainerId") Long trainerId);
    @Query(value = "SELECT * FROM Review r WHERE r.trainer_id = :trainerId ORDER BY r.created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<ReviewEntity> findListByTrainerId(@Param("trainerId") Long trainerId, @Param("limit") int limit, @Param("offset") int offset);
}
