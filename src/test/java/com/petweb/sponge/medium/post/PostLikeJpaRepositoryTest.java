package com.petweb.sponge.medium.post;

import com.petweb.sponge.post.repository.post.PostLikeEntity;
import com.petweb.sponge.post.repository.post.PostLikeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest(showSql = false)
@ActiveProfiles("test")
@Sql("/sql/repository/post-repository-test-data.sql")
@Sql("/sql/repository/postlike-repository-test-data.sql")
public class PostLikeJpaRepositoryTest {

    @Autowired
    private PostLikeJpaRepository postLikeJpaRepository;


    @Test
    public void findLike는_PostId와_로그인아이디로_PostLikeEntity를_찾는다() {

        // given
        Long postId = 1L;
        Long userId = 1L;

        // when
        Optional<PostLikeEntity> result = postLikeJpaRepository.findLike(postId, userId);

        // then
        assertThat(result).isPresent();
        result.ifPresent(like -> {
            assertThat(like.getPostId()).isEqualTo(postId); // postId가 일치하는지 검증
            assertThat(like.getUserId()).isEqualTo(userId); // userId가 일치하는지 검증
        });
    }

    @Test
    public void deleteByPostId는_postId로_PostLike를_삭제한다() {

        // given
        Long postId = 1L;

        // when
        postLikeJpaRepository.deleteByPostId(postId);

        // then
        List<PostLikeEntity> postLikeList = postLikeJpaRepository.findAll();
        assertThat(postLikeList).hasSize(2);
        assertThat(postLikeList).allMatch(postLike -> postLike.getPostId().equals(2L));
    }


}
