package com.petweb.sponge.chat.repository;

import com.petweb.sponge.chat.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    Optional<ChatRoom> findById(Long id);
    List<ChatRoom> findListByTrainerId(Long loginId, int page);

    List<ChatRoom> findListByUserId(Long loginId, int page);
    ChatRoom save(ChatRoom chatRoom);

}
