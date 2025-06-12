package com.petweb.sponge.s3.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private static final ArrayList<String> fileValidate = new ArrayList<>();

    static {
        // 이미지 확장자
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");

        // 동영상 확장자 추가
        fileValidate.add(".mp4");
        fileValidate.add(".avi");
        fileValidate.add(".mov");
        fileValidate.add(".mkv");
    }

    /**
     * 이미지 읽어오기
     *
     * @param imgUrl
     */
    public String readImage(String imgUrl) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucket)
                .key(imgUrl)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(1)) // URL 유효기간 (1분)
                .getObjectRequest(getObjectRequest)
                .build();
        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }

    /**
     * 이미지,동영상 읽어오기
     *
     * @param imgUrlList
     */
    public List<String> readImages(List<String> imgUrlList) {
        List<String> presignedUrlList = new ArrayList<>();

        for (String key : imgUrlList) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(1)) // URL 유효기간 (1분)
                    .getObjectRequest(getObjectRequest)
                    .build();

            PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
            presignedUrlList.add(presignedRequest.url().toString());
        }

        return presignedUrlList;
    }

    /**
     * 파일 저장
     *
     * @param file
     * @param dir
     * @return
     */
    public String saveImage(MultipartFile file, String dir) {
        String originalFilename = dir + "/" + createFileName(file.getOriginalFilename());
        try {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(originalFilename)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return originalFilename;
    }

    /**
     * 복수의 사진,동영상 파일 AWS에 저장
     *
     * @param multipartFile
     * @param dir
     * @return
     */
    public List<String> saveFiles(List<MultipartFile> multipartFile, String dir) {
        List<String> fileNameList = new ArrayList<>();

        multipartFile.forEach(file -> {
            String originalFilename = dir + "/" + createFileName(file.getOriginalFilename());

            try {
                s3Client.putObject(PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(originalFilename)
                                .contentType(file.getContentType())
                                .build(),
                        RequestBody.fromBytes(file.getBytes()));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            fileNameList.add(originalFilename);
        });

        return fileNameList;
    }

    /**
     * 동영상,이미지들 삭제
     *
     * @param fileUrls
     */
    public void deleteFiles(List<String> fileUrls) {
        for (String fileUrl : fileUrls) {
            s3Client.deleteObject(DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileUrl)
                    .build());
        }
    }

    /**
     * 이미지 삭제
     *
     * @param imgUrl
     */
    public void deleteImage(String imgUrl) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(imgUrl)
                .build());
    }

    // 파일명 중복 방지
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.isEmpty()) {
            throw new RuntimeException("INVALID FILE");
        }
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new RuntimeException("INVALID FILE EXTENSION");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }


}
