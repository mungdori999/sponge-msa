package com.petweb.sponge.chat.repository;


import com.petweb.sponge.chat.domain.ChatRoom;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "chat_room")
public class ChatRoomEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String lastChatMsg;
    private Long userId;
    private Long trainerId;
    private Long createdAt;
    private Long modifiedAt;

    public static ChatRoomEntity from(ChatRoom chatRoom) {

        ChatRoomEntity chatRoomEntity = new ChatRoomEntity();
        chatRoomEntity.id = chatRoom.getId();
        chatRoomEntity.lastChatMsg = chatRoom.getLastChatMsg();
        chatRoomEntity.userId = chatRoom.getUserId();
        chatRoomEntity.trainerId = chatRoom.getTrainerId();
        chatRoomEntity.createdAt = chatRoom.getCreatedAt();
        chatRoomEntity.modifiedAt = chatRoom.getModifiedAt();
        return chatRoomEntity;
    }

    public ChatRoom toModel() {
        return ChatRoom.builder()
                .id(id)
                .lastChatMsg(lastChatMsg)
                .userId(userId)
                .trainerId(trainerId)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .build();
    }


}
