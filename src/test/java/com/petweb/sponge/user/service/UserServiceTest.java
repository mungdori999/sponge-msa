package com.petweb.sponge.user.service;

import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.dto.UserUpdate;
import com.petweb.sponge.user.mock.MockUserRepository;
import com.petweb.sponge.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;


class UserServiceTest {

    private UserService userService;

    @BeforeEach()
    void init() {
        UserRepository userRepository = new MockUserRepository();
        userService = UserService.builder()
                .userRepository(userRepository)
                .build();
        userRepository.save(User.builder()
                .email("abc@test.com")
                .name("테스트")
                .createdAt(0L).build());


    }

    @Test
    public void getById는_가입_USER의_정보를_가져온다() {
        // given
        Long id = 1L;

        // when
        User result = userService.getById(id);

        // then
        assertThat(result.getEmail()).isEqualTo("abc@test.com");
        assertThat(result.getCreatedAt()).isEqualTo(0L);
    }

    @Test
    public void update는_USER의_정보를_수정한다() {
        // given
        Long id = 1L;
        UserUpdate userUpdate = UserUpdate.builder()
                .name("test")
                .address("서울 성북구")
                .build();

        // when
        User result = userService.update(id, userUpdate);

        // then
        assertThat(result.getName()).isEqualTo("test");
        assertThat(result.getAddress()).isEqualTo("서울 성북구");

    }

//    @Test
//    public void delete는_USER의_정보를_삭제한다() {
//        // given
//        Long id = 1L;
//
//        // when
//        userService.delete(id);
//
//        // then
//        assertThatThrownBy(() -> userService.getById(id)).isInstanceOf(NotFoundUser.class);
//    }

}