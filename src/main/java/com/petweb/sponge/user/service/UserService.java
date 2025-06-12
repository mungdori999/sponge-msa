package com.petweb.sponge.user.service;

import com.petweb.sponge.exception.error.NotFoundUser;
import com.petweb.sponge.post.repository.post.BookmarkRepository;
import com.petweb.sponge.post.repository.post.PostRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.dto.UserUpdate;
import com.petweb.sponge.user.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Builder
public class UserService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;
    /**
     * 유저 단건 조회
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public User getById(Long id) {
        // user,address 한번에 조회
        return userRepository.findById(id).orElseThrow(
                NotFoundUser::new);
    }

    /**
     * 유저 내정보 조회
     *
     * @param loginId
     * @return
     */
    @Transactional(readOnly = true)
    public User findMyInfo(Long loginId) {
        // user,address 한번에 조회
        return userRepository.findById(loginId).orElseThrow(
                NotFoundUser::new);
    }

    /**
     * 유저 정보 수정
     *
     * @param id
     * @param userUpdate
     * @return
     */
    @Transactional
    public User update(Long id, UserUpdate userUpdate) {
        User user =  userRepository.findById(id).orElseThrow(
                NotFoundUser::new);
        user = user.update(userUpdate);
        user = userRepository.save(user);
        return user;
    }

    /**
     * 유저 정보 삭제
     *
     * @param id
     */
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                NotFoundUser::new);
        userRepository.delete(user);
    }


}
