package com.petweb.sponge.post.domain.post;

import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.post.domain.post.*;
import com.petweb.sponge.post.dto.post.PostCreate;
import com.petweb.sponge.post.dto.post.PostUpdate;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class PostTest {


    @Test
    public void postCreate로_POST를_생설할_수_있다() {
        // given
        PostCreate postCreate = PostCreate.builder()
                .petId(1L)
                .title("산책 친구 구해요")
                .content("이번 주말에 같이 산책할 강아지를 찾습니다!")
                .duration("이상하다")
                .categoryCodeList(List.of(101L, 102L))
                .hashTagList(List.of("강아지", "산책", "서울"))
                .fileUrlList(List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg"))
                .build();

        // when
        Post post = Post.from(1L, 1L, postCreate, new TestClockHolder(12345L));


        // then
        assertThat(post.getUserId()).isEqualTo(1L);
        assertThat(post.getPetId()).isEqualTo(1L);
        assertThat(post.getPostContent().getTitle()).isEqualTo("산책 친구 구해요");
        assertThat(post.getPostContent().getContent()).isEqualTo("이번 주말에 같이 산책할 강아지를 찾습니다!");
        assertThat(post.getPostContent().getDuration()).isEqualTo("이상하다");
        assertThat(post.getPostContent().getCreatedAt()).isEqualTo(12345L);  // TestClockHolder에서 설정한 값 검증

        assertThat(post.getPostCategoryList()).hasSize(2);
        assertThat(post.getPostCategoryList()).extracting("categoryCode")
                .containsExactly(101L, 102L);

        assertThat(post.getTagList()).hasSize(3);
        assertThat(post.getTagList()).extracting("hashtag")
                .containsExactly("강아지", "산책", "서울");

        assertThat(post.getPostFileList()).hasSize(2);
        assertThat(post.getPostFileList()).extracting("fileUrl")
                .containsExactly("https://example.com/image1.jpg", "https://example.com/image2.jpg");

    }

    @Test
    public void postupdate로_POST를_수정할_수_있다() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())
                .userId(1L)
                .petId(1L)
                .postFileList(List.of(
                        PostFile.builder().fileUrl("").build(),
                        PostFile.builder().fileUrl("").build()
                ))
                .tagList(List.of(
                        Tag.builder().hashtag("짖음").build(),
                        Tag.builder().hashtag("건강").build()
                ))
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .build();

        PostUpdate postUpdate = PostUpdate.builder()
                .title("수정된 제목")
                .content("수정된 게시글 내용")
                .duration("3개월")
                .categoryCodeList(List.of(100L, 300L))
                .hashTagList(List.of("강아지", "훈련", "사회화"))
                .fileUrlList(List.of("https://example.com/new_image1.jpg", "https://example.com/new_image2.jpg"))
                .build();

        // when
        post = post.update(postUpdate, new TestClockHolder(12345L));

        // then
        assertThat(post.getPostContent().getTitle()).isEqualTo("수정된 제목");
        assertThat(post.getPostContent().getContent()).isEqualTo("수정된 게시글 내용");
        assertThat(post.getPostContent().getDuration()).isEqualTo("3개월");
        assertThat(post.getPostContent().getModifiedAt()).isEqualTo(12345L);  // TestClockHolder에서 설정한 값 검증

        assertThat(post.getPostCategoryList()).hasSize(2);
        assertThat(post.getPostCategoryList()).extracting("categoryCode")
                .containsExactly(100L, 300L);

        assertThat(post.getTagList()).hasSize(3);
        assertThat(post.getTagList()).extracting("hashtag")
                .containsExactly("강아지", "훈련", "사회화");

        assertThat(post.getPostFileList()).hasSize(2);
        assertThat(post.getPostFileList()).extracting("fileUrl")
                .containsExactly("https://example.com/new_image1.jpg", "https://example.com/new_image2.jpg");
    }

    @Test
    public void 글_작성자와_게시글의_아이디가_동일한지_판단() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())
                .userId(1L)
                .petId(1L)
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .build();

        // when
        // then
        assertThatThrownBy(() -> post.checkUser(2L))
                .isInstanceOf(LoginIdError.class);

        assertThatCode(() -> post.checkUser(1L))
                .doesNotThrowAnyException();
    }

    @Test
    public void 좋아요_수를_증가시킨다() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())
                .userId(1L)
                .petId(1L)
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .build();

        // when
        post.increaseLikeCount();

        // then
        assertThat(post.getLikeCount()).isEqualTo(1);
    }

    @Test
    public void 좋아요_수를_감소시킨다() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())

                .likeCount(1)
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .userId(1L)
                .petId(1L)
                .build();

        // when
        post.decreaseLikeCount();

        // then
        assertThat(post.getLikeCount()).isEqualTo(0);
    }

    @Test
    public void 감소시킬_좋아요가_없으면_오류를_보낸다() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())

                .userId(1L)
                .petId(1L)
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .build();

        // when
        // then
        assertThatThrownBy(post::decreaseLikeCount).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void 답변_수를_증가시킨다() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())

                .userId(1L)
                .petId(1L)
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .build();

        // when
        post.increaseAnswerCount();

        // then
        assertThat(post.getAnswerCount()).isEqualTo(1);
    }

    @Test
    public void 답변_수를_감소시킨다() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())
                .answerCount(1)
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .userId(1L)
                .petId(1L)
                .build();

        // when
        post.decreaseAnswerCount();

        // then
        assertThat(post.getAnswerCount()).isEqualTo(0);
    }

    @Test
    public void 감소시킬_답변_수가_없으면_오류를_보낸다() {
        // given
        Post post = Post.builder()
                .postContent(PostContent.builder()
                        .title("테스트 제목")
                        .content("테스트 게시글 내용")
                        .duration("한달이상")
                        .createdAt(0L)
                        .modifiedAt(0L)
                        .build())

                .userId(1L)
                .petId(1L)
                .postCategoryList(List.of(
                        PostCategory.builder().categoryCode(100L).build(),
                        PostCategory.builder().categoryCode(200L).build()
                ))
                .build();

        // when
        // then
        assertThatThrownBy(post::decreaseAnswerCount).isInstanceOf(IllegalStateException.class);
    }


}
