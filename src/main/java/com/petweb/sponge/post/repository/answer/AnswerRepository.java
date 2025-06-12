package com.petweb.sponge.post.repository.answer;


import com.petweb.sponge.post.domain.answer.Answer;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {
    Optional<Answer> findById(Long id);
    List<Answer> findListByPostId(Long postId);
    List<Answer> findListByTrainerId(Long trainerId, int page);
    Answer save(Answer answer);

    void delete(Answer answer);

}
