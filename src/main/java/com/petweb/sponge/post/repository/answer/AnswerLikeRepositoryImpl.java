package com.petweb.sponge.post.repository.answer;

import com.petweb.sponge.post.domain.answer.AnswerLike;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AnswerLikeRepositoryImpl implements AnswerLikeRepository{

    private final AnswerLikeJpaRepository answerLikeJpaRepository;

    @Override
    public Optional<AnswerLike> findByAnswerId(Long answerId, Long loginId) {
        return answerLikeJpaRepository.findByAnswerId(answerId,loginId).map(AnswerLikeEntity::toModel);
    }

    @Override
    public void save(AnswerLike answerLike) {
        answerLikeJpaRepository.save(AnswerLikeEntity.from(answerLike));
    }

    @Override
    public void delete(AnswerLike answerLike) {
        answerLikeJpaRepository.delete(AnswerLikeEntity.from(answerLike));
    }

    @Override
    public void deleteByAnswerId(Long answerId) {
        answerLikeJpaRepository.deleteByAnswerId(answerId);
    }
}
