package com.petweb.sponge.chat.controller;

import com.petweb.sponge.chat.controller.response.ChatMessageResponse;
import com.petweb.sponge.chat.domain.ChatMessage;
import com.petweb.sponge.chat.dto.ChatMessageCreate;
import com.petweb.sponge.chat.service.ChatMessageService;
import com.petweb.sponge.log.Logging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;

    /**
     * 지금까지의 메세지들을 가져오기
     *
     * @param chatRoomId
     * @param page
     * @return
     */
    @GetMapping("/api/message")
    public ResponseEntity<List<ChatMessageResponse>> getChatRoomMessage(@RequestParam("chatRoomId") Long chatRoomId, int page) {
        List<ChatMessage> chatMessageList = chatMessageService.findListByChatRoomId(chatRoomId, page);
        return new ResponseEntity<>(chatMessageList.stream().map(ChatMessageResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * 메세지
     * @param chatMessageCreate
     * @param accessor
     */
    @MessageMapping("/message")
    public void sendMessage(ChatMessageCreate chatMessageCreate, SimpMessageHeaderAccessor accessor) {
        Long pubId = Long.valueOf(Objects.requireNonNull(accessor.getFirstNativeHeader("id")));
        String loginType = accessor.getFirstNativeHeader("loginType");
        ChatMessage chatMessage = chatMessageService.create(chatMessageCreate, pubId, loginType);

        messagingTemplate.convertAndSend("/sub/channel/" + chatMessageCreate.getChatRoomId(), ChatMessageResponse.from(chatMessage));
    }
}
