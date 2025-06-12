package com.petweb.sponge.post.repository.answer;

import com.petweb.sponge.post.domain.answer.AdoptAnswer;

import java.util.List;
import java.util.Optional;

public interface AdoptAnswerRepository  {


    Optional<AdoptAnswer> findByAnswerId(Long answerId);
    List<AdoptAnswer> findListByTrainerId(Long trainerId);
    List<AdoptAnswer> findListByAnswerIdList(List<Long> answerIdList);
    AdoptAnswer save(AdoptAnswer adoptAnswer);

    void delete(AdoptAnswer adoptAnswer);

}
