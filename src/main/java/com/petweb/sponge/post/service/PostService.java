package com.petweb.sponge.post.service;

import com.petweb.sponge.exception.error.NotFoundPet;
import com.petweb.sponge.exception.error.NotFoundPost;
import com.petweb.sponge.exception.error.NotFoundUser;
import com.petweb.sponge.pet.domain.Pet;
import com.petweb.sponge.pet.repository.PetRepository;
import com.petweb.sponge.post.controller.response.post.PostCheckResponse;
import com.petweb.sponge.post.controller.response.post.PostDetailsResponse;
import com.petweb.sponge.post.domain.post.PostLike;
import com.petweb.sponge.post.domain.post.Bookmark;
import com.petweb.sponge.post.domain.post.Post;
import com.petweb.sponge.post.dto.post.PostCreate;
import com.petweb.sponge.post.dto.post.PostUpdate;
import com.petweb.sponge.post.repository.post.PostLikeRepository;
import com.petweb.sponge.post.repository.post.*;
import com.petweb.sponge.user.domain.User;
import com.petweb.sponge.user.repository.UserRepository;
import com.petweb.sponge.utils.ClockHolder;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Builder
public class PostService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final ClockHolder clockHolder;


    /**
     * 글 단건 조회
     *
     * @param id
     * @return
     */
    @Transactional(readOnly = true)
    public PostDetailsResponse findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                NotFoundPost::new);
        Pet pet = petRepository.findById(post.getPetId()).orElseThrow(
                NotFoundPet::new);
        return PostDetailsResponse.from(post, pet);
    }

    /**
     * 유저 아이디 글 조회
     *
     * @param userId
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Post> findPostListByUserId(Long userId, int page) {
        return postRepository.findListByUserId(userId, page);

    }

    /**
     * 카테고리별 글 전체 조회
     *
     * @param categoryCode
     * @return
     */
    @Transactional(readOnly = true)
    public List<Post> findPostListByCode(Long categoryCode, int page) {
        return postRepository.findListByCode(categoryCode, page);
    }

    /**
     * 검색
     *
     * @param keyword
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Post> search(String keyword, int page) {
        return postRepository.findByKeyword(keyword, page);
    }

    /**
     * 글 작성 저장
     *
     * @param loginId
     * @param postCreate
     * @return
     */
    @Transactional
    public PostDetailsResponse create(Long loginId, PostCreate postCreate) {
        //현재 로그인 유저 정보 가져오기
        User user = userRepository.findById(loginId).orElseThrow(
                NotFoundUser::new);
        //선택한 반려동물 정보 가져오기
        Pet pet = petRepository.findById(postCreate.getPetId()).orElseThrow(
                NotFoundPet::new);
        Post post = Post.from(user.getId(), pet.getId(), postCreate, clockHolder);
        post = postRepository.save(post);
        return PostDetailsResponse.from(post, pet);
    }

    /**
     * 글 수정
     *
     * @param loginId
     * @param id
     * @param postUpdate
     * @return
     */
    @Transactional
    public PostDetailsResponse update(Long loginId, Long id, PostUpdate postUpdate) {
        Post post = postRepository.findById(id).orElseThrow(
                NotFoundPost::new);
        Pet pet = petRepository.findById(post.getPetId()).orElseThrow(
                NotFoundPet::new);
        post.checkUser(loginId);
        postRepository.initPost(post.getId());
        post = post.update(postUpdate, clockHolder);
        post = postRepository.save(post);
        return PostDetailsResponse.from(post, pet);
    }


    /**
     * 글 삭제
     *
     * @param loginId
     * @param id
     */
    @Transactional
    public void delete(Long loginId, Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                NotFoundPost::new);
        post.checkUser(loginId);
        bookmarkRepository.deleteByPostId(post.getId());
        postLikeRepository.deleteByPostId(post.getId());
        postRepository.delete(post);
    }

    /**
     * 문제행동글 관련 파일 삭제
     *
     * @param loginId
     * @param problemPostId
     * @param fileUrlList
     */
    @Transactional
    public void deletePostFiles(Long loginId, Long problemPostId, List<String> fileUrlList) {
//        PostEntity postEntity = postRepository.findById(problemPostId).orElseThrow(NotFoundPost::new);
//        if (!postEntity.getUserEntity().getId().equals(loginId)) {
//            throw new LoginIdError();
//        }
//        postFileRepository.deleteByFiles(fileUrlList);
    }


    /**
     * 북마크,좋아요가 눌러있는지 없는지 조회
     *
     * @param loginId
     * @param postId
     * @return
     */
    @Transactional(readOnly = true)
    public PostCheckResponse findCheck(Long loginId, Long postId) {
        Optional<PostLike> like = postLikeRepository.findLike(postId, loginId);
        Optional<Bookmark> bookmark = bookmarkRepository.findBookmark(postId, loginId);
        return PostCheckResponse.from(like, bookmark);

    }

    /**
     * 북마크 저장되어있는 글 조회
     *
     * @param loginId
     * @param page
     * @return
     */
    @Transactional(readOnly = true)
    public List<Post> findPostListByBookmark(Long loginId, int page) {
        List<Bookmark> bookmarkList = bookmarkRepository.findBookmarkList(loginId, page);
        return postRepository.findListByPostIdList(bookmarkList.stream().map(Bookmark::getPostId).collect(Collectors.toList()));
    }

    /**
     * 북마크 업데이트
     *
     * @param loginId
     * @param postId
     */
    @Transactional
    public void updateBookmark(Long loginId, Long postId) {
        Optional<Bookmark> bookmark = bookmarkRepository.findBookmark(postId, loginId);
        if (bookmark.isPresent()) {
            bookmarkRepository.delete(bookmark.get());
        } else {
            Bookmark newBookmark = Bookmark.from(postId, loginId,clockHolder);
            bookmarkRepository.save(newBookmark);
        }
    }

    /**
     * 추천수 업데이트
     *
     * @param loginId
     * @param postId
     */
    @Transactional
    public void updateLike(Long loginId, Long postId) {
        Optional<PostLike> like = postLikeRepository.findLike(postId, loginId);
        Post post = postRepository.findById(postId).orElseThrow(
                NotFoundPost::new);
        /**
         * 추천이 이미 있다면 추천을 삭제 추천수 -1
         * 추천이 없다면 추천을 저장 추천수 +1
         */
        if (like.isPresent()) {
            post.decreaseLikeCount();
            postLikeRepository.delete(like.get());
            postRepository.save(post);
        } else {
            PostLike newPostLike = PostLike.from(postId, loginId);
            post.increaseLikeCount();
            postLikeRepository.save(newPostLike);
            postRepository.save(post);
        }
    }


}
