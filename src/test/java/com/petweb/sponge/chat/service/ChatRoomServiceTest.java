package com.petweb.sponge.chat.service;

import com.petweb.sponge.chat.controller.response.ChatRoomResponse;
import com.petweb.sponge.chat.domain.ChatRoom;
import com.petweb.sponge.chat.dto.ChatRoomCreate;
import com.petweb.sponge.chat.mock.MockChatRoomRepository;
import com.petweb.sponge.chat.repository.ChatRoomRepository;
import com.petweb.sponge.trainer.domain.History;
import com.petweb.sponge.trainer.domain.Trainer;
import com.petweb.sponge.trainer.domain.TrainerAddress;
import com.petweb.sponge.trainer.mock.MockTrainerRepository;
import com.petweb.sponge.trainer.repository.TrainerRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.mock.MockUserRepository;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.ClockHolder;
import com.petweb.sponge.utils.Gender;
import com.petweb.sponge.utils.LoginType;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class ChatRoomServiceTest {

    private ChatRoomService chatRoomService;


    @BeforeEach
    public void init() {
        TrainerRepository trainerRepository = new MockTrainerRepository();
        UserRepository userRepository = new MockUserRepository();
        ClockHolder clockHolder = new TestClockHolder(12345L);
        ChatRoomRepository chatRoomRepository = new MockChatRoomRepository();
        chatRoomService = ChatRoomService.builder()
                .chatRoomRepository(chatRoomRepository)
                .userRepository(userRepository)
                .trainerRepository(trainerRepository)
                .clockHolder(clockHolder)
                .build();

        userRepository.save(User.builder()
                .email("abc@test.com")
                .name("테스트")
                .createdAt(0L).build());
        trainerRepository.save(Trainer.builder()
                .email("test1@naver.com")
                .name("test1")
                .gender(Gender.MALE.getCode())
                .profileImgUrl("")
                .content("안녕")
                .years(2)
                .createdAt(0L)
                .adoptCount(7)
                .trainerAddressList(List.of(
                        TrainerAddress.builder()
                                .city("서울")
                                .town("강남구")
                                .build()
                ))
                .historyList(List.of(History.builder()
                        .title("프로젝트 A")
                        .startDt("202401")
                        .endDt("202406")
                        .description("Spring Boot 기반 웹 애플리케이션 개발")
                        .build()))
                .build());
        chatRoomRepository.save(ChatRoom.builder()
                .userId(1L)
                .lastChatMsg("")
                .trainerId(1L)
                .createdAt(0L)
                .modifiedAt(0L)
                .build());
    }

    @Test
    public void findMyInfo는_나의_채팅방을_찾는다() {
        // given
        Long loginId = 1L;
        String loginType = LoginType.TRAINER.getLoginType();
        int page = 0;

        // when
        List<ChatRoomResponse> result = chatRoomService.findMyInfo(loginId, loginType, page);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
        assertThat(result).allSatisfy(chatRoom -> {
            assertThat(chatRoom.getLoginType()).isEqualTo(LoginType.USER.getLoginType());
            assertThat(chatRoom.getLastMsg()).isNotNull();
            assertThat(chatRoom.getCreatedAt()).isNotNull();
        });

    }

    @Test
    public void create는_ChatRoom을_저장한다() {
        // given
        Long loginId = 1L;
        ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                .trainerId(1L).build();

        // when
        ChatRoom result = chatRoomService.create(loginId, chatRoomCreate);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(loginId);
        assertThat(result.getTrainerId()).isEqualTo(chatRoomCreate.getTrainerId());
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getModifiedAt()).isNotNull();
    }
}
