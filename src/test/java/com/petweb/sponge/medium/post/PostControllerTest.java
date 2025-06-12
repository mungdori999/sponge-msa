package com.petweb.sponge.medium.post;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petweb.sponge.exception.error.NotFoundPost;
import com.petweb.sponge.jwt.JwtUtil;
import com.petweb.sponge.jwt.Token;
import com.petweb.sponge.post.dto.post.PostCreate;
import com.petweb.sponge.post.dto.post.PostUpdate;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@ActiveProfiles("test")
public class PostControllerTest {


    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtUtil jwtUtil;

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 게시글을_단건으로_가져온다() throws Exception {
        // given
        Long postId = 1L;
        // when
        // then
        mockMvc.perform(get("/api/post/{id}", postId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postId))
                .andExpect(jsonPath("$.title").value("반려견의 공격성 문제"))
                .andExpect(jsonPath("$.content").value("반려견이 다른 강아지나 사람을 공격하는 문제를 해결하고 싶어요."))
                .andExpect(jsonPath("$.duration").value("3개월"))
                .andExpect(jsonPath("$.createdAt").value(1))
                .andExpect(jsonPath("$.modifiedAt").value(0))
                .andExpect(jsonPath("$.likeCount").value(0))
                .andExpect(jsonPath("$.answerCount").value(0))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.pet.id").value(1))
                .andExpect(jsonPath("$.pet.name").value("바둑이"))
                .andExpect(jsonPath("$.postFileList").isArray())
                .andExpect(jsonPath("$.postFileList.length()").value(1))
                .andExpect(jsonPath("$.tagList").isArray())
                .andExpect(jsonPath("$.tagList.length()").value(2))
                .andExpect(jsonPath("$.postCategoryList").isArray())
                .andExpect(jsonPath("$.postCategoryList.length()").value(1));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가쓴_글들을_가져온다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/post/user").param("userId", "1").param("page", "0"))
                .andExpect(jsonPath("$.size()").value(10))
                .andExpect(status().isOk());
    }


    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 내가쓴_글들을_가져온다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();
        // when
        // then
        mockMvc.perform(get("/api/post/my_info").param("page", "1")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 카테고리별로_글들을_가져온다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(get("/api/post").param("categoryCode", "100").param("page", "0"))
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(status().isOk());
    }


    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 검색어로_글들을_가져온다() throws Exception {
        // given
        String keyword = "반려견의 긍정적인 훈련";
        // when
        // then
        mockMvc.perform(get("/api/post/search").param("keyword", keyword).param("page", "0"))

                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(13))
                .andExpect(jsonPath("$[0].title").value("반려견의 긍정적인 훈련"))
                .andExpect(jsonPath("$[0].content").value("반려견의 행동을 좋게 만드는 방법에 대해 조언을 구하고 싶습니다."));
    }


    @SqlGroup({
            @Sql(value = "/sql/controller/user-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_글을_작성한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        PostCreate postCreate = PostCreate.builder()
                .petId(1L)
                .title("산책 친구 구해요")
                .content("이번 주말에 같이 산책할 강아지를 찾습니다!")
                .duration("이상하다")
                .categoryCodeList(List.of(100L, 200L))
                .hashTagList(List.of("강아지", "산책", "서울"))
                .fileUrlList(List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"))
                .build();
        // when
        // then
        mockMvc.perform(post("/api/post").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postCreate)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/post/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("산책 친구 구해요"))
                .andExpect(jsonPath("$.content").value("이번 주말에 같이 산책할 강아지를 찾습니다!"))
                .andExpect(jsonPath("$.duration").value("이상하다"))
                .andExpect(jsonPath("$.likeCount").value(0))
                .andExpect(jsonPath("$.answerCount").value(0))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.pet.id").value(1))
                .andExpect(jsonPath("$.pet.name").value("바둑이"))
                .andExpect(jsonPath("$.postFileList").isArray())
                .andExpect(jsonPath("$.postFileList.length()").value(2))
                .andExpect(jsonPath("$.tagList").isArray())
                .andExpect(jsonPath("$.tagList.length()").value(3))
                .andExpect(jsonPath("$.postCategoryList").isArray())
                .andExpect(jsonPath("$.postCategoryList.length()").value(2));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_글을_수정한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        PostUpdate postUpdate = PostUpdate.builder()
                .title("수정된 제목")
                .content("수정된 게시글 내용")
                .duration("3개월")
                .categoryCodeList(List.of(100L, 300L))
                .hashTagList(List.of("강아지", "훈련", "사회화"))
                .fileUrlList(List.of("https://example.com/new_image1.jpg", "https://example.com/new_image2.jpg"))
                .build();
        // when
        // then
        mockMvc.perform(patch("/api/post/1").header(HttpHeaders.AUTHORIZATION, accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postUpdate)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/post/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("수정된 제목"))
                .andExpect(jsonPath("$.content").value("수정된 게시글 내용"))
                .andExpect(jsonPath("$.duration").value("3개월"))
                .andExpect(jsonPath("$.likeCount").value(0))
                .andExpect(jsonPath("$.answerCount").value(0))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.pet.id").value(1))
                .andExpect(jsonPath("$.pet.name").value("바둑이"))
                .andExpect(jsonPath("$.postFileList").isArray())
                .andExpect(jsonPath("$.postFileList.length()").value(2))
                .andExpect(jsonPath("$.tagList").isArray())
                .andExpect(jsonPath("$.tagList.length()").value(3))
                .andExpect(jsonPath("$.postCategoryList").isArray())
                .andExpect(jsonPath("$.postCategoryList.length()").value(2));
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_글을_삭제한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        // when
        // then
        mockMvc.perform(delete("/api/post/1").header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/post/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertThat(result.getResolvedException())
                        .isInstanceOf(NotFoundPost.class));

    }


    @SqlGroup({
            @Sql(value = "/sql/controller/postlike-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/bookmark-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_북마크와_좋아요를_조회한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        // when
        // then
        mockMvc.perform(get("/api/post/check").param("postId", "1")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCheck").value(true))
                .andExpect(jsonPath("$.bookmarkCheck").value(true));

    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/bookmark-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_북마크한_글들을_조회한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        // when
        // then
        mockMvc.perform(get("/api/post/bookmark").param("page","1" )
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(status().isOk());
    }

    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/bookmark-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_북마크를_업데이트_한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        // when
        // then
        mockMvc.perform(post("/api/post/bookmark").param("postId","1" )
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/post/check").param("postId", "1")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCheck").value(false))
                .andExpect(jsonPath("$.bookmarkCheck").value(false));
    }


    @SqlGroup({
            @Sql(value = "/sql/controller/post-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/controller/pet-controller-test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
            @Sql(value = "/sql/delete-all-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    })
    @Test
    public void 유저가_좋아요를_업데이트_한다() throws Exception {
        // given
        Token token = jwtUtil.createToken(1L, "김칠칠", LoginType.USER.getLoginType());
        String accessToken = token.getAccessToken();

        // when
        // then
        mockMvc.perform(post("/api/post/like").param("postId","1" )
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/post/check").param("postId", "1")
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCheck").value(true));
        mockMvc.perform(get("/api/post/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount").value(1));
    }
}
