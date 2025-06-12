package com.petweb.sponge.post.repository.answer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdoptAnswerJpaRepository extends JpaRepository<AdoptAnswerEntity,Long> {

    @Query("SELECT ae FROM  AdoptAnswerEntity  ae WHERE ae.trainerId = :trainerId")
    List<AdoptAnswerEntity> findListByTrainerId(@Param("trainerId") Long trainerId);

    @Query("SELECT ae FROM AdoptAnswerEntity ae WHERE ae.answerId IN :answerIdList")
    List<AdoptAnswerEntity> findListByAnswerIdList(@Param("answerIdList") List<Long> answerIdList);

}
