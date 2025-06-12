package com.petweb.sponge.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity,Long> {

    @Query(value = "SELECT * FROM chat_message WHERE chat_room_id = :chatRoomId ORDER BY created_at DESC LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<ChatMessageEntity> findListByChatRoomId(@Param("chatRoomId") Long chatRoomId,@Param("limit") int limit, @Param("offset") int offset);

}
