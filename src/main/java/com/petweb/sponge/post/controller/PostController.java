package com.petweb.sponge.post.controller;

import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.post.controller.response.post.PostCheckResponse;
import com.petweb.sponge.post.controller.response.post.PostDetailsResponse;
import com.petweb.sponge.post.controller.response.post.PostListResponse;
import com.petweb.sponge.post.domain.post.Post;
import com.petweb.sponge.post.dto.post.PostCreate;
import com.petweb.sponge.post.dto.post.PostUpdate;
import com.petweb.sponge.post.service.PostService;
import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Logging
public class PostController {


    private final PostService postService;
    private final AuthorizationUtil authorizationUtil;

    /**
     * 글 단건 조회
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDetailsResponse> getById(@PathVariable("id") Long id) {
        PostDetailsResponse post = postService.findById(id);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    /**
     * 유저 글 조회
     *
     * @param userId
     * @param page
     * @return
     */
    @GetMapping("/user")
    public ResponseEntity<List<PostListResponse>> getListByUserId(@RequestParam("userId") Long userId, @RequestParam("page") int page) {
        List<Post> postList = postService.findPostListByUserId(userId, page);
        return new ResponseEntity<>(postList.stream().map(PostListResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * 내가쓴글 조회
     *
     * @return
     */
    @GetMapping("/my_info")
    @UserAuth
    public ResponseEntity<List<PostListResponse>> getMyPost(@RequestParam("page") int page) {
        List<Post> postList = postService.findPostListByUserId(authorizationUtil.getLoginId(), page);
        return new ResponseEntity<>(postList.stream().map(PostListResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * 카테고리별 글 전체조회
     *
     * @param categoryCode
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<PostListResponse>> getAllPost(@RequestParam("categoryCode") Long categoryCode, @RequestParam("page") int page) {
        List<Post> postList = postService.findPostListByCode(categoryCode, page);
        return new ResponseEntity<>(postList.stream().map(PostListResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * 검색 기능
     *
     * @param keyword
     * @param page
     * @return
     */
    @GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<PostListResponse>> search(@RequestParam("keyword") String keyword, @RequestParam("page") int page) {
        List<Post> postList = postService.search(keyword, page);
        return new ResponseEntity<>(postList.stream().map(PostListResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * 글 작성 저장
     *
     * @param postCreate
     * @return
     */
    @PostMapping
    @UserAuth
    public ResponseEntity<PostDetailsResponse> create(@RequestBody PostCreate postCreate) {
        PostDetailsResponse post = postService.create(authorizationUtil.getLoginId(), postCreate);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    /**
     * 글 수정
     *
     * @param id
     * @param postUpdate
     * @return
     */
    @PatchMapping("/{id}")
    @UserAuth
    public ResponseEntity<PostDetailsResponse> update(@PathVariable("id") Long id, @RequestBody PostUpdate postUpdate) {
        PostDetailsResponse post = postService.update(authorizationUtil.getLoginId(), id, postUpdate);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    /**
     * 글 삭제
     *
     * @param id
     */
    @DeleteMapping("/{id}")
    @UserAuth
    public void delete(@PathVariable("id") Long id) {
        postService.delete(authorizationUtil.getLoginId(), id);
    }

    /**
     * 북마크, 좋아요 눌러져있는지 조회
     *
     * @param postId
     * @return
     */
    @GetMapping("/check")
    @UserAuth
    public ResponseEntity<PostCheckResponse> getMyCheck(@RequestParam("postId") Long postId) {
        PostCheckResponse postCheckResponse = postService.findCheck(authorizationUtil.getLoginId(), postId);
        return new ResponseEntity<>(postCheckResponse, HttpStatus.OK);
    }

    /**
     * 내가 저장한 글 조회
     *
     * @param page
     * @return
     */
    @GetMapping("/bookmark")
    @UserAuth
    public ResponseEntity<List<PostListResponse>> getBookmarkPost(@RequestParam("page") int page) {
        List<Post> postList = postService.findPostListByBookmark(authorizationUtil.getLoginId(), page);
        return new ResponseEntity<>(postList.stream().map(PostListResponse::from).collect(Collectors.toList()), HttpStatus.OK);
    }

    /**
     * 북마크 업데이트
     *
     * @param postId
     */
    @PostMapping("/bookmark")
    @UserAuth
    public void updateBookmark(@RequestParam("postId") Long postId) {
        postService.updateBookmark(authorizationUtil.getLoginId(), postId);
    }

    /**
     * 추천수 업데이트
     *
     * @param postId
     */
    @PostMapping("/like")
    @UserAuth
    public void updateLike(@RequestParam("postId") Long postId) {
        postService.updateLike(authorizationUtil.getLoginId(), postId);
    }


}