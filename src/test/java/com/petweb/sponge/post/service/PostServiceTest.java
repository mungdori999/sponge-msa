package com.petweb.sponge.post.service;

import com.petweb.sponge.pet.domain.Pet;
import com.petweb.sponge.pet.mock.MockPetRepository;
import com.petweb.sponge.pet.repository.PetRepository;
import com.petweb.sponge.post.controller.response.post.PostCheckResponse;
import com.petweb.sponge.post.controller.response.post.PostDetailsResponse;
import com.petweb.sponge.post.domain.post.*;
import com.petweb.sponge.post.dto.post.PostCreate;
import com.petweb.sponge.post.dto.post.PostUpdate;
import com.petweb.sponge.post.mock.post.MockBookmarkRepository;
import com.petweb.sponge.post.mock.post.MockPostLikeRepository;
import com.petweb.sponge.post.mock.post.MockPostRepository;
import com.petweb.sponge.post.repository.post.BookmarkRepository;
import com.petweb.sponge.post.repository.post.PostLikeRepository;
import com.petweb.sponge.post.repository.post.PostRepository;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.mock.MockUserRepository;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.Gender;
import com.petweb.sponge.utils.TestClockHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class PostServiceTest {

    private PostService postService;

    @BeforeEach()
    void init() {
        PetRepository petRepository = new MockPetRepository();
        UserRepository userRepository = new MockUserRepository();
        PostRepository postRepository = new MockPostRepository();
        BookmarkRepository bookmarkRepository = new MockBookmarkRepository();
        PostLikeRepository postLikeRepository = new MockPostLikeRepository();

        postService = PostService.builder()
                .petRepository(petRepository)
                .userRepository(userRepository)
                .postRepository(postRepository)
                .bookmarkRepository(bookmarkRepository)
                .postLikeRepository(postLikeRepository)
                .clockHolder(new TestClockHolder(12345L)).build();

        User user = userRepository.save(User.builder()
                .email("abc@test.com")
                .name("테스트")
                .createdAt(0L).build());
        Pet pet = petRepository.save(Pet.builder()
                .name("테스트 이름")
                .gender(Gender.MALE.getCode())
                .breed("테스트 견종")
                .age(5)
                .userId(user.getId())
                .createdAt(0L)
                .build());

        for (int i = 0; i < 15; i++) {
            postRepository.save(Post.builder()
                    .postContent(PostContent.builder()
                            .title("테스트 제목")
                            .content("테스트 게시글 내용")
                            .duration("한달이상")
                            .createdAt(0L)
                            .modifiedAt(0L)
                            .build())
                    .userId(user.getId())
                    .petId(pet.getId())
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
                    .build());

        }

    }

    @Test
    public void findById는_POST를_조회한다() {
        // given
        Long id = 1L;

        // when
        PostDetailsResponse result = postService.findById(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("테스트 제목");
        assertThat(result.getContent()).isEqualTo("테스트 게시글 내용");
        assertThat(result.getDuration()).isEqualTo("한달이상");
        assertThat(result.getCreatedAt()).isEqualTo(0L);
        assertThat(result.getModifiedAt()).isEqualTo(0L);

        assertThat(result.getPostCategoryList()).hasSize(2);
        assertThat(result.getTagList().get(0).getHashtag()).isEqualTo("짖음");


    }

    @Test
    public void findPostListByUserId은_글을작성한_user로_PostList를_가져온다() {
        // given
        Long userId = 1L;
        int page = 0;

        // when
        List<Post> result = postService.findPostListByUserId(userId, page);

        // then
        assertThat(result).hasSize(15);
    }

    @Test
    public void findPostListByCode은_카테고리코드로_PostList를_가져온다() {
        // given
        Long categoryCode = 100L;
        int page = 0;

        // when
        List<Post> result = postService.findPostListByCode(categoryCode, page);

        // then
        assertThat(result).hasSize(15);
    }

    @Test
    public void search은_검색어로_PostList를_가져온다() {

        // given
        String keyword = "테스트";
        int page = 0;

        // when
        List<Post> result = postService.search(keyword, page);

        // then
        assertThat(result).hasSize(15);

    }

    @Test
    public void create는_POST의_정보를_저장한다() {

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
        PostDetailsResponse post = postService.create(1L, postCreate);

        // then
        assertThat(post).isNotNull();
        assertThat(post.getTitle()).isEqualTo("산책 친구 구해요");
        assertThat(post.getContent()).isEqualTo("이번 주말에 같이 산책할 강아지를 찾습니다!");
        assertThat(post.getId()).isEqualTo(16L);
    }

    @Test
    public void update는_POST의_정보를_수정한다() {

        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .title("수정된 제목")
                .content("수정된 게시글 내용")
                .duration("3개월")
                .categoryCodeList(List.of(100L, 300L))
                .hashTagList(List.of("강아지", "훈련", "사회화"))
                .fileUrlList(List.of("https://example.com/new_image1.jpg", "https://example.com/new_image2.jpg"))
                .build();

        // when
        PostDetailsResponse result = postService.update(1L, 1L, postUpdate);

        // then
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getContent()).isEqualTo("수정된 게시글 내용");
        assertThat(result.getDuration()).isEqualTo("3개월");
        assertThat(result.getPostCategoryList().get(1).getCategoryCode()).isEqualTo(300L);
        assertThat(result.getPostCategoryList()).hasSize(2);

    }

    @Test
    public void delete는_POST를_삭제한다() {
        // given
        Long userId = 1L;
        Long id = 1L;
        int page = 0;

        // when
        postService.delete(userId, id);

        // then
        List<Post> postList = postService.findPostListByUserId(userId, page);
        assertThat(postList).hasSize(14);
    }

    @Test
    public void findCheck는_체크사항들을_조회한다() {
        // given
        Long userId = 1L;
        Long postId = 1L;

        // when
        PostCheckResponse result = postService.findCheck(userId, postId);

        // then
        assertThat(result.isBookmarkCheck()).isFalse();
        assertThat(result.isLikeCheck()).isFalse();
    }

    @Test
    public void findPostListByBookmark는_북마크되어있는_글을조회한다() {
        // given
        Long userId = 1L;
        int page = 0;
        for (int i = 1; i <= 12; i++) {
            postService.updateBookmark(userId, (long) i);
        }

        // when
        List<Post> result = postService.findPostListByBookmark(userId, page);

        // then
        assertThat(result).hasSize(10);


    }

    @Test
    public void 북마크를안한상태에서_updateBookmark는_BOOKMARK를_추가한다() {
        // given
        Long userId = 1L;
        Long postId = 1L;

        // when
        postService.updateBookmark(userId, postId);

        // given
        PostCheckResponse check = postService.findCheck(userId, postId);
        assertThat(check.isBookmarkCheck()).isTrue();

    }

    @Test
    public void 북마크를한상태에서_updateBookmark는_BOOKMARK를_추가한다() {
        // given
        Long userId = 1L;
        Long postId = 1L;
        postService.updateBookmark(userId, postId);

        // when
        postService.updateBookmark(userId, postId);

        // given
        PostCheckResponse check = postService.findCheck(userId, postId);
        assertThat(check.isBookmarkCheck()).isFalse();

    }

}