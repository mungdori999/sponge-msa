package com.petweb.sponge.pet.controller;

import com.petweb.sponge.auth.UserAuth;
import com.petweb.sponge.log.Logging;
import com.petweb.sponge.pet.service.PetService;
import com.petweb.sponge.s3.service.S3Service;
import com.petweb.sponge.utils.AuthorizationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet/image")
@Logging
public class PetImageController {
    private final S3Service s3Service;
    private final PetService petService;
    private final  AuthorizationUtil authorizationUtil;


    /**
     * 반려견 이미지 조회
     * @param imgUrl
     * @return
     */
    @GetMapping()
    public ResponseEntity<String> getByImageUrl(@RequestParam("imgUrl")String imgUrl) {
        String presignedUrl = s3Service.readImage(imgUrl);
        return new ResponseEntity<>(presignedUrl,HttpStatus.OK);
    }


    /**
     * 반려견 이미지 저장
     *
     * @param multipartFile
     * @return
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @UserAuth
    public ResponseEntity<String> uploadPetImg(@RequestPart MultipartFile multipartFile) {
        String saveFile = s3Service.saveImage(multipartFile, "pet");
        return new ResponseEntity<>(saveFile, HttpStatus.OK);
    }

    /**
     * 반려견 이미지 삭제
     *
     * @param petId
     * @param imgUrl
     */
    @DeleteMapping("")
    @UserAuth
    public void deletePetImg(@RequestParam("petId") Long petId, @RequestParam("imgUrl") String imgUrl) {
        // S3에서 삭제
        s3Service.deleteImage(imgUrl);
        // 펫 이미지 링크 삭제
        petService.deletePetImg(authorizationUtil.getLoginId(), petId);
    }
}
