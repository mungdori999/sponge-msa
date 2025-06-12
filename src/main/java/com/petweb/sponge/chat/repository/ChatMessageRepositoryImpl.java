package com.petweb.sponge.chat.repository;

import com.petweb.sponge.chat.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepository {

    private final ChatMessageJpaRepository chatMessageJpaRepository;
    private static final int PAGE_SIZE = 20;  // 페이지당 항목 수


    @Override
    public List<ChatMessage> findListByChatRoomId(Long chatRoomId, int page) {
        int offset = page * PAGE_SIZE;
        return chatMessageJpaRepository.findListByChatRoomId(chatRoomId,PAGE_SIZE ,offset).stream().map(ChatMessageEntity::toModel).toList();
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        return chatMessageJpaRepository.save(ChatMessageEntity.from(chatMessage)).toModel();
    }
}
