package com.petweb.sponge.trainer.controller;

import com.petweb.sponge.auth.TrainerAuth;
import com.petweb.sponge.exception.error.LoginIdError;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.s3.service.S3Service;
import com.petweb.sponge.trainer.service.TrainerService;
import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/trainer/image")
@Logging
public class TrainerImageController {

    private final S3Service s3Service;
    private final TrainerService trainerService;
    private final AuthorizationUtil authorizationUtil;

    /**
     * 훈련사 이미지 조회
     * @param imgUrl
     * @return
     */
    @GetMapping()
    public ResponseEntity<String> getByImageUrl(@RequestParam("imgUrl")String imgUrl) {
        String presignedUrl = s3Service.readImage(imgUrl);
        return new ResponseEntity<>(presignedUrl,HttpStatus.OK);
    }

    /**
     * 훈련사 이미지 저장
     *
     * @param multipartFile
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadTrainerImg(@RequestPart MultipartFile multipartFile) {
        String saveFile = s3Service.saveImage(multipartFile, "profile");
        return new ResponseEntity<>(saveFile, HttpStatus.OK);
    }

    /**
     * 훈련사 이미지 삭제
     * @param trainerId
     * @param imgUrl
     */
    @DeleteMapping("")
    @TrainerAuth
    public void deleteTrainerImg(@RequestParam("trainerId") Long trainerId, @RequestParam("imgUrl") String imgUrl) {
        if (authorizationUtil.getLoginId().equals(trainerId)) {
            // S3에서 삭제
            s3Service.deleteImage(imgUrl);
            //훈련사 이미지 링크 삭제
            trainerService.deleteImgUrl(trainerId);
        } else {
            throw new LoginIdError();
        }

    }
}
