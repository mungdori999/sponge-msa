package com.petweb.sponge.user.repository;

import com.petweb.sponge.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id).map(UserEntity::toModel);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email).map(UserEntity::toModel);
    }

    @Override
    public List<User> findByUserIdList(List<Long> userIdList) {
        return userJpaRepository.findByIdList(userIdList).stream().map(UserEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(UserEntity.from(user)).toModel();
    }

    @Override
    public User register(User user) {
        UserEntity userEntity = UserEntity.builder()
                .email(user.getEmail())
                .name(user.getName())
                .createdAt(user.getCreatedAt())
                .build();
        return userJpaRepository.save(userEntity).toModel();
    }


    @Override
    public void delete(User user) {
        userJpaRepository.delete(UserEntity.from(user));
    }
}
