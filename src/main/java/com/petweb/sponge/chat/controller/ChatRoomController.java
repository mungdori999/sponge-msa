package com.petweb.sponge.chat.controller;

import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.chat.controller.response.ChatRoomResponse;
import com.petweb.sponge.chat.dto.ChatRoomCreate;
import com.petweb.sponge.chat.service.ChatRoomService;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatroom")
@Slf4j
@Logging
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final AuthorizationUtil authorizationUtil;

    @GetMapping("/my_info")
    public ResponseEntity<List<ChatRoomResponse>> getMyChatRoom(@RequestParam("page")  int page) {
        List<ChatRoomResponse> chatRoomList = chatRoomService.findMyInfo(authorizationUtil.getLoginId(), authorizationUtil.getLoginType(), page);
        return new ResponseEntity<>(chatRoomList, HttpStatus.OK);
    }



    /**
     * 개인 DM 채팅방 생성
     * @param chatRoomCreate
     */
    @PostMapping("")
    @UserAuth
    public void createPersonalChatRoom(@RequestBody ChatRoomCreate chatRoomCreate) {
        chatRoomService.create(authorizationUtil.getLoginId(), chatRoomCreate);
    }
}
