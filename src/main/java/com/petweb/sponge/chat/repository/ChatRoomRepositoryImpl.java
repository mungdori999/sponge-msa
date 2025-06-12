package com.petweb.sponge.chat.repository;


import com.petweb.sponge.chat.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepository {

    private final ChatRoomJpaRepository chatRoomJpaRepository;
    private static final int PAGE_SIZE = 10;  // 페이지당 항목 수

    @Override
    public Optional<ChatRoom> findById(Long id) {
        return chatRoomJpaRepository.findById(id).map(ChatRoomEntity::toModel);
    }

    @Override
    public List<ChatRoom> findListByTrainerId(Long loginId, int page) {
        int offset = page * PAGE_SIZE;
        return chatRoomJpaRepository.findListByTrainerId(loginId, PAGE_SIZE, offset).stream().map(ChatRoomEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public List<ChatRoom> findListByUserId(Long loginId, int page) {
        int offset = page * PAGE_SIZE;
        return chatRoomJpaRepository.findListByUserId(loginId, PAGE_SIZE, offset).stream().map(ChatRoomEntity::toModel).collect(Collectors.toList());
    }

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return chatRoomJpaRepository.save(ChatRoomEntity.from(chatRoom)).toModel();
    }
}
