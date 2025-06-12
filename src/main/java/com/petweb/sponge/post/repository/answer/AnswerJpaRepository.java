package com.petweb.sponge.post.repository.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnswerJpaRepository extends JpaRepository<AnswerEntity, Long> {


    @Query("SELECT a FROM AnswerEntity a WHERE a.postId = :postId")
    List<AnswerEntity> findListByPostId(@Param("postId") Long postId);

    @Query(value = "SELECT * FROM Answer a WHERE a.trainer_id = :trainerId ORDER BY a.created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<AnswerEntity> findListByTrainerId(@Param("trainerId") Long trainerId, @Param("limit") int limit, @Param("offset") int offset);
}
