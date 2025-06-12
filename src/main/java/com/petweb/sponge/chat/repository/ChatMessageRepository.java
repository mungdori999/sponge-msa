package com.petweb.sponge.chat.repository;

import com.petweb.sponge.chat.domain.ChatMessage;

import java.util.List;


public interface ChatMessageRepository {

    List<ChatMessage> findListByChatRoomId(Long chatRoomId, int page);

    ChatMessage save(ChatMessage chatMessage);
}
