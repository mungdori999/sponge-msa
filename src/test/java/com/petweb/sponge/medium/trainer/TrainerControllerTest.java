package com.petweb.sponge.medium.trainer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.trainer.dto.*;
import com.petweb.sponge.utils.Gender;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class TrainerControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @SqlGroup({
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사_정보를_단건으로_조회한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/trainer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("trainer1@test.com"))
                .andExpect(jsonPath("$.name").value("김트레이너"))
                .andExpect(jsonPath("$.gender").value(1))
                .andExpect(jsonPath("$.phone").value("01012345678"))
                .andExpect(jsonPath("$.profileImgUrl").value(""))
                .andExpect(jsonPath("$.content").value("안녕하세요. 트레이너입니다."))
                .andExpect(jsonPath("$.years").value(5))
                .andExpect(jsonPath("$.adoptCount").value(0))
                .andExpect(jsonPath("$.score").value(0.0))
                .andExpect(jsonPath("$.chatCount").value(0))
                .andExpect(jsonPath("$.trainerAddressList").isArray())
                .andExpect(jsonPath("$.trainerAddressList.length()").value(3))
                .andExpect(jsonPath("$.historyList").isArray())
                .andExpect(jsonPath("$.historyList.length()").value(3));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사가_내정보를_조회한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김트레이너", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();

        // when
        // then
        mockMvc.perform(get("/api/trainer/my_info").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("trainer1@test.com"))
                .andExpect(jsonPath("$.name").value("김트레이너"))
                .andExpect(jsonPath("$.gender").value(1))
                .andExpect(jsonPath("$.phone").value("01012345678"))
                .andExpect(jsonPath("$.profileImgUrl").value(""))
                .andExpect(jsonPath("$.content").value("안녕하세요. 트레이너입니다."))
                .andExpect(jsonPath("$.years").value(5))
                .andExpect(jsonPath("$.adoptCount").value(0))
                .andExpect(jsonPath("$.score").value(0.0))
                .andExpect(jsonPath("$.chatCount").value(0))
                .andExpect(jsonPath("$.trainerAddressList").isArray())
                .andExpect(jsonPath("$.trainerAddressList.length()").value(3))
                .andExpect(jsonPath("$.historyList").isArray())
                .andExpect(jsonPath("$.historyList.length()").value(3));
    }


//    @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
//    @Test
//    public void 훈련사_회원가입을한다() throws Exception {
//        // given
//        TrainerCreate trainerCreate = TrainerCreate.builder()
//                .email("test@example.com")
//                .name("김철수")
//                .gender(Gender.MALE.getCode())  // 1: 남성, 2: 여성
//                .phone("01012345678")
//                .profileImgUrl("")
//                .content("안녕하세요! 전문 트레이너입니다.")
//                .years(5) // 연차
//                .trainerAddressList(List.of(
//                        TrainerAddressCreate.builder()
//                                .city("서울")
//                                .town("강남구")
//                                .build(),
//                        TrainerAddressCreate.builder()
//                                .city("부산")
//                                .town("해운대구")
//                                .build()
//                ))
//                .historyList(List.of(
//                        HistoryCreate.builder()
//                                .title("헬스 트레이너")
//                                .startDt("201901")
//                                .endDt("202312")
//                                .description("헬스 트레이너로 5년간 활동")
//                                .build(),
//                        HistoryCreate.builder()
//                                .title("PT 강사")
//                                .startDt("202105")
//                                .endDt("202312")
//                                .description("개인 PT 강사로 활동")
//                                .build()
//                ))
//                .build();
//
//        // when
//        // then
//        MvcResult result = mockMvc.perform(post("/api/trainer")
//                        .contentType(MediaType.APPLICATION_JSON)  // JSON 요청 설정
//                        .content(new ObjectMapper().writeValueAsString(trainerCreate)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("김철수"))
//                .andExpect(jsonPath("$.refreshToken").exists()).andReturn();
//
//        String responseBody = result.getResponse().getContentAsString();
//        String refreshToken = JsonPath.read(responseBody, "$.refreshToken");
//
//        String name = jwtUtil.getName(refreshToken);
//        // name 값 검증
//        assertThat(trainerCreate.getName()).isEqualTo(name);
//    }

    @SqlGroup({
            @Sql(value = "/sql/controller/trainer-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 훈련사가_정보를_업데이트한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김트레이너", LoginType.TRAINER.getLoginType());
        String accessToken = token.getAccessToken();
        TrainerUpdate trainerUpdate = TrainerUpdate.builder()
                .name("김철수")
                .gender(Gender.MALE.getCode())
                .phone("01012345678")
                .profileImgUrl("")
                .content("안녕하세요! 훈련사입니다")
                .years(3)
                .trainerAddressList(List.of(
                        TrainerAddressUpdate.builder()
                                .city("서울")
                                .town("도봉산")
                                .build()
                ))
                .historyList(List.of(
                        HistoryUpdate.builder()
                                .title("훈련사")
                                .startDt("201901")
                                .endDt("202312")
                                .description("헬스 트레이너로 5년간 활동")
                                .build()
                ))
                .build();

        // when
        // then
        MvcResult result = mockMvc.perform(patch("/api/trainer/1").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)  // JSON 요청 설정
                        .content(new ObjectMapper().writeValueAsString(trainerUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refreshToken").exists()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String refreshToken = JsonPath.read(responseBody, "$.refreshToken");

        String name = jwtUtil.getName(refreshToken);
        // name 값 검증
        assertThat(trainerUpdate.getName()).isEqualTo(name);
    }

}
