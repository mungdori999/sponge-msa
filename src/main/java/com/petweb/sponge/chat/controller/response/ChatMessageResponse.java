package com.petweb.sponge.chat.controller.response;

import com.petweb.sponge.chat.domain.ChatMessage;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageResponse {

    private Long id;
    private String message;
    private Long pubId;
    private String loginType;
    private Long createdAt;

    public static ChatMessageResponse from(ChatMessage chatMessage) {
            return ChatMessageResponse.builder()
                    .id(chatMessage.getId())
                    .message(chatMessage.getMessage())
                    .pubId(chatMessage.getPubId())
                    .loginType(chatMessage.getLoginType())
                    .createdAt(chatMessage.getCreatedAt()).build();
    }
}
