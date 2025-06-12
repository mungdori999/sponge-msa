package com.petweb.sponge.medium.chat;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.petweb.sponge.chat.dto.ChatRoomCreate;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.utils.LoginType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class ChatRoomJpaControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @SqlGroup({
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/chatroom-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 내_채팅방들을_가져온다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/chatroom/my_info").param("page", "0")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_훈련사와의_채팅방을_만든다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        ChatRoomCreate chatRoomCreate = ChatRoomCreate.builder()
                .trainerId(1L)
                .build();
        // when
        // then
        mockMvc.perform(post("/api/chatroom")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(chatRoomCreate)))
                .andExpect(status().isOk());
    }
}
