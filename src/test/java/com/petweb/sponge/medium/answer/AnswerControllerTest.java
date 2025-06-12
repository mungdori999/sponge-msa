package com.petweb.sponge.medium.answer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.post.dto.answer.AdoptAnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerCreate;
import com.petweb.sponge.post.dto.answer.AnswerUpdate;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/adoptanswer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/answer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 문제행동글에_적힌_훈련사_답변을_모두_가져온다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/answer").param("postId", "1"))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/adoptanswer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/answer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사가_작성한_답변을_모두_가져온다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/answer/trainer").param("trainerId", "1").param("page", "0"))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/adoptanswer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/answer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사가_작성한_나의_답변을_모두_가져온다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/answer/my_info").param("page", "0")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(status().isOk());


    }


    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사가_답변을_작성한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        AnswerCreate answerCreate = AnswerCreate.builder()
                .postId(1L)
                .content("새로운 내용입니다")
                .build();

        // when
        // then
        mockMvc.perform(post("/api/answer").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(answerCreate)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/answer/trainer").param("trainerId", "1").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].answerResponse.content").value("새로운 내용입니다"));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/answer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사가_답변을_수정한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        AnswerUpdate answerUpdate = AnswerUpdate.builder()
                .content("수정 내용입니다").build();

        // when
        // then
        mockMvc.perform(patch("/api/answer/20").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(answerUpdate)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/answer/trainer").param("trainerId", "1").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].answerResponse.content").value("수정 내용입니다"));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사가_답변을_삭제한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        AnswerCreate answerCreate = AnswerCreate.builder()
                .postId(1L)
                .content("새로운 내용입니다")
                .build();

        // when
        // then
        mockMvc.perform(post("/api/answer").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(answerCreate)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/api/answer/1").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/answer/trainer").param("trainerId", "1").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }


    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/answer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_훈련사답변을_채택한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        AdoptAnswerCreate answerCreate = AdoptAnswerCreate.builder()
                .answerId(20L)
                .postId(10L)
                .trainerId(1L)
                .build();
        // when
        // then
        mockMvc.perform(post("/api/answer/adopt").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(answerCreate)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/answer/trainer").param("trainerId", "1").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].checkAdopt").value(true));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/answer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_훈련사답변_추천을_조회한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/answer/check").param("answerId", "1")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("likeCheck").value(false));

    }


    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/answer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_훈련사답변_추천을_업데이트한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(post("/api/answer/like").param("answerId", "20")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/answer/check").param("answerId", "20")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("likeCheck").value(true));

    }


}
