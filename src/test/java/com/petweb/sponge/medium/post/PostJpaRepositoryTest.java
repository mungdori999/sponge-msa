package com.petweb.sponge.medium.post;


import com.petweb.sponge.post.repository.post.PostEntity;
import com.petweb.sponge.post.repository.post.PostJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/post-repository-test-data.sql")
public class PostJpaRepositoryTest {

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Test
    public void findPostById는_게시물관련_모든애그리거트_조인해서_조회한다() {

        // given
        Long postId = 1L;

        // when
        Optional<PostEntity> result = postJpaRepository.findPostById(postId);

        // then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(postId);
        assertThat(result.get().getTitle()).isEqualTo("반려견의 공격성 문제");
        assertThat(result.get().getPostCategoryEntityList()).hasSize(1);
        assertThat(result.get().getTagEntityList()).hasSize(2);

    }

    @Test
    public void findListByUserId는_유저가_작성한_모든_글을_가져온다() {
        // given
        Long userId = 1L;
        int page = 1;

        // when
        List<PostEntity> result = postJpaRepository.findListByUserId(userId, page);

        // then
        assertThat(result.get(0).getId()).isEqualTo(5L);
        assertThat(result).hasSize(5);

    }

    @Test
    public void findListByKeyword는_검색어로_게시물을_조회한다() {
        // given
        String keyword = "반려견행동";
        int page = 0;

        // when
        List<PostEntity> result = postJpaRepository.findListByKeyword(keyword, page);

        // then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getTagEntityList().get(0).getHashtag()).isEqualTo(keyword);
    }

    @Test
    public void findListByCode는_카테고리코드로_게시물울_조회한다() {
        // given
        Long categoryCode = 200L;
        int page = 0;

        // when
        List<PostEntity> result = postJpaRepository.findListByCode(categoryCode, page);

        // then
        assertThat(result).hasSize(4);
        assertThat(result.get(0).getPostCategoryEntityList().get(0).getCategoryCode()).isEqualTo(categoryCode);
    }

    @Test
    public void findListByIdList는_카테고리코드로_게시물울_조회한다() {
        // given
        List<Long> idList = new ArrayList<>(List.of(1L, 2L, 3L));

        // when
        List<PostEntity> result = postJpaRepository.findListByIdList(idList);

        // then
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getId()).isEqualTo(3L);
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(2).getId()).isEqualTo(1L);
    }

    @Test
    public void init은_해당_포스트의_모든_데이터를_초기화한다() {
        // given
        Long postId = 1L;

        // when
        postJpaRepository.initPost(postId);

        // then
        Optional<PostEntity> result = postJpaRepository.findById(postId);

        assertThat(result).isPresent();
        assertThat(result.get().getPostCategoryEntityList()).isEmpty();

    }
}
