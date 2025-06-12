package com.petweb.sponge.medium.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.petweb.sponge.exception.error.LoginTypeError;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.user.dto.UserUpdate;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@SqlGroup({
        @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
        @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
})
public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void 유저를_단건으로_조회한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("김칠칠"));
    }

    @Test
    public void 유저가_내정보를_조회한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/user/my_info").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("김칠칠"));
    }

    @Test
    public void 유저가_정보를_업데이트한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        UserUpdate userUpdate = UserUpdate.builder()
                .name("김수정")
                .build();
        // when
        // then
        MvcResult result = mockMvc.perform(patch("/api/user/1").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)  // JSON 요청 설정
                        .content(new ObjectMapper().writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").exists()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String refreshToken = JsonPath.read(responseBody, "$.refreshToken");

        String name = jwtUtil.getName(refreshToken);
        // name 값 검증
        assertThat(userUpdate.getName()).isEqualTo(name);

    }

    @Test
    public void 훈련사가_유저의메소드를_잘못_호출한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/user/my_info").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isUnauthorized())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(LoginTypeError.class));
    }


}
