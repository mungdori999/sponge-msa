package com.petweb.sponge.medium.user;

import com.petweb.sponge.user.repository.UserEntity;
import com.petweb.sponge.user.repository.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/user-repository-test-data.sql")
public class UserJpaRepositoryTest {

    @Autowired
    private UserJpaRepository userJpaRepository;


    @Test
    public void findByEmail은_email로_유저를_찾는다() {
        // given
        String email = "user1@test.com";

        // when
        Optional<UserEntity> result = userJpaRepository.findByEmail(email);

        // then
        assertThat(result).isPresent(); // 값이 존재하는지 검증
        assertThat(result.get().getEmail()).isEqualTo(email);
    }

    @Test
    public void findByIdList는id여러개로_유저를_찾는다() {
        // given
        List<Long> idList = new ArrayList<>(List.of(1L, 2L));

        // when
        List<UserEntity> result = userJpaRepository.findByIdList(idList);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("user1@test.com");
        assertThat(result.get(1).getEmail()).isEqualTo("user2@test.com");

    }


}
