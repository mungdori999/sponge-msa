package com.petweb.sponge.user.domain;

import com.petweb.sponge.user.dto.UserUpdate;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.*;

public class UserTest {


    @Test
    public void userUpdate로_USER를_업데이트_할_수_있다() {
        // given
        User user = User.builder()
                .id(1L)
                .email("test@naver.com")
                .name("김철수")
                .address("")
                .createdAt(0L)
                .build();
        UserUpdate userUpdate = UserUpdate.builder()
                .name("김업데이트")
                .address("임시")
                .build();
        // when
        user  = user.update(userUpdate);

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("test@naver.com"); // 이메일은 업데이트되지 않음
        assertThat(user.getName()).isEqualTo("김업데이트");
        assertThat(user.getAddress()).isEqualTo("임시");
        assertThat(user.getCreatedAt()).isNotNull(); // 생성일은 유지되어야 함

    }
}
