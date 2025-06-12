package com.petweb.sponge.trainer.service;

import com.petweb.sponge.exception.error.NotFoundTrainer;
import com.petweb.sponge.exception.error.NotFoundUser;
import com.petweb.sponge.trainer.controller.response.ReviewCheckResponse;
import com.petweb.sponge.trainer.controller.response.ReviewResponse;
import com.petweb.sponge.trainer.domain.Review;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.dto.ReviewCreate;
import com.petweb.sponge.trainer.repository.ReviewRepository;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final ClockHolder clockHolder;


    /**
     * 내가 이 훈련사에게 리뷰를 썼는지 체크
     *
     * @param loginId
     * @param trainerId
     * @return
     */
    public ReviewCheckResponse findCheck(Long loginId, Long trainerId) {
        Optional<Review> review = reviewRepository.findByUserId(loginId, trainerId);
        return ReviewCheckResponse.from(review);
    }

    /**
     * 리뷰 조회
     *
     * @param trainerId
     * @param page
     * @return
     */
    public List<ReviewResponse> getListByTrainerId(Long trainerId, int page) {
        List<Review> reviewList = reviewRepository.findListByTrainerId(trainerId, page);
        List<User> userList = userRepository.findByUserIdList(reviewList.stream().map(Review::getUserId).collect(Collectors.toList()));
        return reviewList.stream()
                .flatMap(review -> userList.stream()
                        .filter(user -> review.getUserId().equals(user.getId()))
                        .map(user -> ReviewResponse.from(review, user)))
                .collect(Collectors.toList());

    }

    /**
     * 리뷰 데이터 생성
     *
     * @param loginId
     * @param reviewCreate
     * @return
     */
    @Transactional
    public Review create(Long loginId, ReviewCreate reviewCreate) {
        User user = userRepository.findById(loginId).orElseThrow(
                NotFoundUser::new);
        Trainer trainer = trainerRepository.findShortById(reviewCreate.getTrainerId())
                .orElseThrow(NotFoundTrainer::new);
        Review review = Review.from(user.getId(), reviewCreate, clockHolder);
        // 리뷰 계산
        trainer.calcReview(review.getScore());
        trainerRepository.save(trainer);
        return reviewRepository.save(review);
    }


}
