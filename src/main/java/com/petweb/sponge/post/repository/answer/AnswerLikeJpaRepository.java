package com.petweb.sponge.post.repository.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AnswerLikeJpaRepository extends JpaRepository<AnswerLikeEntity, Long> {


    @Query("SELECT l FROM AnswerLikeEntity l WHERE l.answerId = :answerId AND l.userId = :loginId")
    Optional<AnswerLikeEntity> findByAnswerId(@Param("answerId") Long answerId, @Param("loginId") Long loginId);

    @Query("DELETE AnswerLikeEntity l where l.answerId= :answerId")
    @Modifying
    void deleteByAnswerId(@Param("answerId") Long answerId);
}
