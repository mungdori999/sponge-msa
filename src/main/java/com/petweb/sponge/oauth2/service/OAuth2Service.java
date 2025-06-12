package com.petweb.sponge.oauth2.service;

import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.ClockHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

    private final UserRepository userRepository;
    private final TrainerRepository trainerRepository;
    private final ClockHolder clockHolder;

    public User loadUser(LoginOAuth2 loginOAuth2) {

        User existData = userRepository.findByEmail(loginOAuth2.getEmail()).orElse(null);
        //첫 로그인일 시
        if (existData == null) {
            User user = User.builder()
                    .email(loginOAuth2.getEmail())
                    .name(loginOAuth2.getName())
                    .createdAt(clockHolder.clock())
                    .build();
            User savedUser = userRepository.register(user);
            return savedUser;
        }
        //이미 정보가 있을 시
        else {
            return existData;
        }
    }

    public Trainer checkTrainer(LoginOAuth2 loginOAuth2) {
        Trainer existData = trainerRepository.findByEmail(loginOAuth2.getEmail()).orElse(null);
        return existData;
    }
}
