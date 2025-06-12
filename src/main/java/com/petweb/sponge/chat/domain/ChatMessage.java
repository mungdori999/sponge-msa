package com.petweb.sponge.chat.domain;

import com.petweb.sponge.chat.dto.ChatMessageCreate;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ChatMessage {

    private Long id;
    private String message;
    private Long pubId;
    private String loginType;
    private Long chatRoomId;
    private Long createdAt;

    @Builder
    public ChatMessage(Long id, String message, Long pubId, String loginType, Long chatRoomId, Long createdAt) {
        this.id = id;
        this.message = message;
        this.pubId = pubId;
        this.loginType = loginType;
        this.chatRoomId = chatRoomId;
        this.createdAt = createdAt;
    }

    public static ChatMessage from(ChatMessageCreate chatMessageCreate, Long pubId, String loginType, ClockHolder clockHolder) {
        return ChatMessage.builder()
                .message(chatMessageCreate.getMessage())
                .pubId(pubId)
                .loginType(loginType)
                .chatRoomId(chatMessageCreate.getChatRoomId())
                .createdAt(clockHolder.clock())
                .build();
    }
}
