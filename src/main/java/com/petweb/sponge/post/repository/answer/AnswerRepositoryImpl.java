package com.petweb.sponge.post.repository.answer;

import com.petweb.sponge.post.domain.answer.Answer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AnswerRepositoryImpl implements AnswerRepository {

    private final AnswerJpaRepository answerJpaRepository;
    private static final int PAGE_SIZE = 10;  // 페이지당 항목 수

    @Override
    public Optional<Answer> findById(Long id) {
        return answerJpaRepository.findById(id).map(AnswerEntity::toModel);
    }

    @Override
    public List<Answer> findListByPostId(Long postId) {
        return answerJpaRepository.findListByPostId(postId).stream().map(AnswerEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<Answer> findListByTrainerId(Long trainerId, int page) {
        int offset = page * PAGE_SIZE;
        return answerJpaRepository.findListByTrainerId(trainerId,PAGE_SIZE ,offset).stream().map(AnswerEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Answer save(Answer answer) {
        return answerJpaRepository.save(AnswerEntity.from(answer)).toModel();
    }

    @Override
    public void delete(Answer answer) {
        answerJpaRepository.delete(AnswerEntity.from(answer));
    }
}
