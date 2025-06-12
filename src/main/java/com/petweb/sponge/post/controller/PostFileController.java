package com.petweb.sponge.post.controller;

import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.post.service.PostService;
import com.petweb.sponge.s3.dto.FileListCreate;
import com.petweb.sponge.s3.service.S3Service;
import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post/file")
@Logging
public class PostFileController {

    private final S3Service s3Service;
    private final PostService postService;
    private final AuthorizationUtil authorizationUtil;


    /**
     * 게시글 이미지,동영상 조회
     *
     * @param imgUrlList
     * @return
     */
    @GetMapping()
    public ResponseEntity<List<String>> getByImageUrlList(@RequestParam("imgUrl") List<String> imgUrlList) {
        List<String> presignedUrlList = s3Service.readImages(imgUrlList);
        return new ResponseEntity<>(presignedUrlList, HttpStatus.OK);
    }


    /**
     * 게시글 이미지,동영상 업로드
     *
     * @param multipartFileList
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UserAuth
    public ResponseEntity<List<String>> uploadPostFiles(@RequestPart List<MultipartFile> multipartFileList) {
        List<String> saveFiles = s3Service.saveFiles(multipartFileList, "post");
        return new ResponseEntity<>(saveFiles, HttpStatus.OK);
    }


    /**
     * 게시글 이미지,동영상 삭제
     *
     * @param fileListCreate
     */
    @DeleteMapping("/{postId}")
    @UserAuth
    public void deletePostFile(@PathVariable Long postId, @RequestBody FileListCreate fileListCreate) {
        // S3에서 삭제
        s3Service.deleteFiles(fileListCreate.getFileUrlList());
        // DB에서 링크 삭제
        postService.deletePostFiles(authorizationUtil.getLoginId(), postId, fileListCreate.getFileUrlList());
    }
}
