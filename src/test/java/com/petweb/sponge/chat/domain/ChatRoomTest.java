package com.petweb.sponge.chat.domain;

import com.petweb.sponge.chat.dto.ChatRoomCreate;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ChatRoomTest {


    @Test
    public void chatCreate로_ChatRoom을_생성한다() {
        // given
        ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                .trainerId(1L).build();
        // when
        ChatRoom result = ChatRoom.from(chatRoomCreate, 1L, new TestClockHolder(12345L));
        // then
        assertThat(result.getTrainerId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getCreatedAt()).isEqualTo(12345L);
        assertThat(result.getModifiedAt()).isEqualTo(0L);
    }
    @Test
    public void chatRoom의_마지막채팅을_바꾼다() {

        // given
        String lastMsg = "마지막 채팅";

        ChatRoom chatRoom = ChatRoom.builder()
                .id(1L)
                .userId(1L)
                .lastChatMsg("")
                .trainerId(1L)
                .createdAt(0L)
                .modifiedAt(0L)
                .build();

        // when
        chatRoom = chatRoom.update(lastMsg, new TestClockHolder(12345L));

        // then
        assertThat(chatRoom.getLastChatMsg()).isEqualTo(lastMsg);
        assertThat(chatRoom.getModifiedAt()).isEqualTo(12345L);

    }
}
