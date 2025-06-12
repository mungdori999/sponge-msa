package com.petweb.sponge.chat.service;

import com.petweb.sponge.chat.controller.response.ChatRoomResponse;
import com.petweb.sponge.chat.domain.ChatRoom;
import com.petweb.sponge.chat.dto.ChatRoomCreate;
import com.petweb.sponge.chat.repository.ChatRoomRepository;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.ClockHolder;
import com.petweb.sponge.utils.LoginType;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Builder
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    /**
     * 나의 채팅방 조회
     *
     * @param loginId
     * @param loginType
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<ChatRoomResponse> findMyInfo(Long loginId, String loginType, int page) {
        if (loginType.equals(LoginType.TRAINER.getLoginType())) {
            List<ChatRoom> chatRoomList = chatRoomRepository.findListByTrainerId(loginId, page);
            List<User> userList = userRepository.findByUserIdList(chatRoomList.stream().map(ChatRoom::getUserId).collect(Collectors.toList()));

            Map<Long, String> userNameMap = userList.stream()
                    .collect(Collectors.toMap(User::getId, User::getName));

            return chatRoomList.stream()
                    .map(chatRoom -> {
                        String name = userNameMap.getOrDefault(chatRoom.getUserId(), "");
                        String lastMsg = chatRoom.getLastChatMsg();
                        Long createdAt = chatRoom.getModifiedAt() != 0 ? chatRoom.getModifiedAt() : chatRoom.getCreatedAt();
                        return ChatRoomResponse.from(chatRoom.getId(), name, "", LoginType.USER.getLoginType(), lastMsg, createdAt);
                    })
                    .toList();
        } else {
            List<ChatRoom> chatRoomList = chatRoomRepository.findListByUserId(loginId, page);
            List<Trainer> trainerList = trainerRepository.findShortByIdList(chatRoomList.stream().map(ChatRoom::getTrainerId).collect(Collectors.toList()));

            Map<Long, String> trainerNameMap = trainerList.stream()
                    .collect(Collectors.toMap(Trainer::getId, Trainer::getName));


            return chatRoomList.stream()
                    .map(chatRoom -> {
                        String name = trainerNameMap.getOrDefault(chatRoom.getTrainerId(), "");
                        String lastMsg = chatRoom.getLastChatMsg();
                        Long createdAt = chatRoom.getModifiedAt() != 0 ? chatRoom.getModifiedAt() : chatRoom.getCreatedAt();
                        return ChatRoomResponse.from(chatRoom.getId(), name, "", LoginType.TRAINER.getLoginType(), lastMsg, createdAt);
                    })
                    .toList();

        }
    }

    /**
     * 개인 DM 채팅방 생성
     *
     * @param loginId
     * @param chatRoomCreate
     * @return
     */
    @Transactional
    public ChatRoom create(Long loginId, ChatRoomCreate chatRoomCreate) {
        ChatRoom chatRoom = ChatRoom.from(chatRoomCreate, loginId, clockHolder);
        return chatRoomRepository.save(chatRoom);
    }
}
