package com.petweb.sponge.trainer.repository;

import com.petweb.sponge.trainer.domain.Review;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {
    private final ReviewJpaRepository reviewJpaRepository;
    private static final int PAGE_SIZE = 10;

    @Override
    public Optional<Review> findByUserId(Long loginId, Long trainerId) {
        return reviewJpaRepository.findByUserId(loginId, trainerId).map(ReviewEntity::toModel);
    }

    @Override
    public List<Review> findListByTrainerId(Long trainerId, int page) {
        int offset = page * PAGE_SIZE;
        return reviewJpaRepository.findListByTrainerId(trainerId, PAGE_SIZE,offset).stream().map(ReviewEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public Review save(Review review) {
        return reviewJpaRepository.save(ReviewEntity.from(review)).toModel();
    }
}
