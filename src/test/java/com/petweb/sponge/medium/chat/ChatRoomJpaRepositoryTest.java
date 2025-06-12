package com.petweb.sponge.medium.chat;

import com.petweb.sponge.chat.repository.ChatRoomEntity;
import com.petweb.sponge.chat.repository.ChatRoomJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/chatroom-repository-test-data.sql")
public class ChatRoomJpaRepositoryTest {


    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;
    private final static int PAGE_SIZE = 10;


    @Test
    public void findListByTrainerId는_trainerId로_chatRoom을_찾는다() {
        // given
        Long loginId = 1L;
        int page = 0;
        int offset = page * PAGE_SIZE;

        // when
        List<ChatRoomEntity> result = chatRoomJpaRepository.findListByTrainerId(loginId, PAGE_SIZE, offset);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isLessThanOrEqualTo(PAGE_SIZE);
        assertThat(result.stream().allMatch(chatRoom -> chatRoom.getTrainerId().equals(loginId)))
                .isTrue();

    }

    @Test
    public void findListByUserId는_userId로_chatRoom을_찾는다() {
        // given
        Long loginId = 1L;
        int page = 0;
        int offset = page * PAGE_SIZE;

        // when
        List<ChatRoomEntity> result = chatRoomJpaRepository.findListByUserId(loginId, PAGE_SIZE, offset);

        // then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isLessThanOrEqualTo(PAGE_SIZE);
        assertThat(result.stream().allMatch(chatRoom -> chatRoom.getUserId().equals(loginId)))
                .isTrue();

    }
}
