package com.petweb.sponge.user.repository;

import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.repository.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository  {
    Optional<User> findById(Long id);
    Optional<User> findByEmail(String email);
    List<User> findByUserIdList(List<Long> userIdList);
    User save(User user);
    User register(User user);
    void delete(User user);

}
