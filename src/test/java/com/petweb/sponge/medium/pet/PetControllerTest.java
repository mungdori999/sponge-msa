package com.petweb.sponge.medium.pet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petweb.sponge.exception.error.NotFoundPet;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.pet.dto.PetCreate;
import com.petweb.sponge.pet.dto.PetUpdate;
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

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @SqlGroup({
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 반려동물_정보를_단건으로_조회한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("바둑이"))
                .andExpect(jsonPath("$.breed").value("진돗개"))
                .andExpect(jsonPath("$.gender").value(1))
                .andExpect(jsonPath("$.age").value(3))
                .andExpect(jsonPath("$.weight").value(15.2))
                .andExpect(jsonPath("$.petImgUrl").value("http://example.com/dog1.jpg"));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_가지고있는_반려동물_정보를_전체_조회한다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/pet").param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("바둑이"))
                .andExpect(jsonPath("$[1].name").value("뽀삐"))
                .andExpect(jsonPath("$").isArray())  // 전체 응답이 배열임을 검증
                .andExpect(jsonPath("$.length()").value(2));

    }

    @SqlGroup({
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 내가_가지고있는_반려동물_정보를_전체_조회한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/pet/my_info").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("바둑이"))
                .andExpect(jsonPath("$[1].name").value("뽀삐"))
                .andExpect(jsonPath("$").isArray())  // 전체 응답이 배열임을 검증
                .andExpect(jsonPath("$.length()").value(2));

    }

    @SqlGroup({
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 반려동물을_등록한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        PetCreate petCreate = PetCreate.builder()
                .name("테스트 이름")
                .breed("테스트 견종")
                .gender(Gender.MALE.getCode())
                .age(5)
                .weight(10.5f)
                .petImgUrl("")
                .build();

        // when
        // then
        mockMvc.perform(post("/api/pet").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(petCreate)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("테스트 이름"))
                .andExpect(jsonPath("$.breed").value("테스트 견종"))
                .andExpect(jsonPath("$.gender").value(Gender.MALE.getCode()))
                .andExpect(jsonPath("$.age").value(5))
                .andExpect(jsonPath("$.weight").value(10.5))
                .andExpect(jsonPath("$.petImgUrl").value(""));

    }

    @SqlGroup({
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_자신의_반려견_정보를_수정한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        PetUpdate petUpdate = PetUpdate.builder()
                .name("수정 이름")
                .breed("수정 견종")
                .gender(Gender.NEUTERED_MALE.getCode())
                .age(15)
                .weight(20.4f)
                .petImgUrl("https").build();


        // when
        // then
        mockMvc.perform(patch("/api/pet/1").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(petUpdate)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("수정 이름"))
                .andExpect(jsonPath("$.breed").value("수정 견종"))
                .andExpect(jsonPath("$.gender").value(Gender.NEUTERED_MALE.getCode()))
                .andExpect(jsonPath("$.age").value(15))
                .andExpect(jsonPath("$.weight").value(20.4))
                .andExpect(jsonPath("$.petImgUrl").value("https"));

    }

    @SqlGroup({
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_자신의_반려견_정보를_삭제한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        // when
        // then
        mockMvc.perform(delete("/api/pet/1").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/pet/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(NotFoundPet.class));

    }


}
