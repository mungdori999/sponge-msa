package com.petweb.sponge.chat.domain;

import com.petweb.sponge.chat.dto.ChatMessageCreate;
import com.petweb.sponge.utils.LoginType;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ChatMessageTest {


    @Test
    public void ChatMessageCreate로_ChatMessage를_생성한다() {
        // given
        ChatMessageCreate chatMessageCreate = ChatMessageCreate.builder()
                .chatRoomId(1L)
                .message("메세지")
                .build();
        // when
        ChatMessage result = ChatMessage.from(chatMessageCreate, 1L, LoginType.USER.getLoginType(), new TestClockHolder(12345L));

        // then
        assertThat(result.getChatRoomId()).isEqualTo(1L);
        assertThat(result.getMessage()).isEqualTo("메세지");
        assertThat(result.getPubId()).isEqualTo(1L);
        assertThat(result.getLoginType()).isEqualTo(LoginType.USER.getLoginType());
        assertThat(result.getCreatedAt()).isEqualTo(12345L);
    }
}
