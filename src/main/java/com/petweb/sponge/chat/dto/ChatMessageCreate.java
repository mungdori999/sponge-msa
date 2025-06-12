package com.petweb.sponge.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatMessageCreate {


    private Long chatRoomId;
    private String message;
}
