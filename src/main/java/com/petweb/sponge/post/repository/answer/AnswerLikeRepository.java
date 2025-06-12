package com.petweb.sponge.post.repository.answer;

import com.petweb.sponge.post.domain.answer.AnswerLike;

import java.util.Optional;

public interface AnswerLikeRepository {

    Optional<AnswerLike> findByAnswerId(Long answerId, Long loginId);

    void save(AnswerLike answerLike);
    void delete(AnswerLike answerLike);

    void deleteByAnswerId(Long answerId);
}
