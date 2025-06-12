package com.petweb.sponge.medium.trainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.trainer.dto.ReviewCreate;
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
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;


    @SqlGroup({
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_훈련사에게_리뷰를_썼는지_체크한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/review/check").param("trainerId", "1")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("reviewCheck").value(false));
    }


    @SqlGroup({
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/review-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사에게_달린_리뷰들을_조회한다() throws Exception {
        // given

        // when
        // then
        mockMvc.perform(get("/api/review").param("trainerId", "1").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].score").value(3));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/review-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 나에게_달린_리뷰들을_조회한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/review/my_info/trainer").param("page", "0")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("[0].score").value(3));
    }


    @SqlGroup({
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_리뷰를_작성한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        ReviewCreate reviewCreate = ReviewCreate.builder()
                .trainerId(1L)
                .score(3)
                .content("별로에요").build();
        // when
        // then
        mockMvc.perform(post("/api/review")
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(reviewCreate)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/review").param("trainerId", "1").param("page", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("[0].score").value(3))
                .andExpect(jsonPath("[0].content").value("별로에요"));
    }


}
