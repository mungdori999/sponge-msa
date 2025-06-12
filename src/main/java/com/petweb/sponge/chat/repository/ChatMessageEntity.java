package com.petweb.sponge.chat.repository;


import com.petweb.sponge.chat.domain.ChatMessage;
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
@Table(name = "chat_message")
public class ChatMessageEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String message;
    private Long pubId;
    private String loginType;
    private Long chatRoomId;
    private Long createdAt;


    public static ChatMessageEntity from(ChatMessage chatMessage) {
        ChatMessageEntity chatMessageEntity = new ChatMessageEntity();
        chatMessageEntity.message = chatMessage.getMessage();
        chatMessageEntity.pubId = chatMessage.getPubId();
        chatMessageEntity.loginType = chatMessage.getLoginType();
        chatMessageEntity.chatRoomId = chatMessage.getChatRoomId();
        chatMessageEntity.createdAt = chatMessage.getCreatedAt();
        return chatMessageEntity;

    }

    public ChatMessage toModel() {
        return ChatMessage.builder()
                .id(id)
                .message(message)
                .pubId(pubId)
                .loginType(loginType)
                .chatRoomId(chatRoomId)
                .createdAt(createdAt).build();
    }
}
