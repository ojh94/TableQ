package com.itschool.tableq.service.base;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.itschool.tableq.domain.base.IncludeFileUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class BaseServiceWithS3<Req, Res, Entity extends IncludeFileUrl> extends BaseService<Req, Res, Entity>{
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadFile(MultipartFile file, String directoryName, String newFileName) throws IOException, SdkClientException, AmazonServiceException {
        // 디렉토리 경로 포함한 S3 객체 키 설정
        String fileKey = directoryName + "/" + newFileName;

        // S3 URL 생성
        String fileUrl = "https://" + bucket + ".s3.amazonaws.com/" + fileKey;

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 파일 업로드
        amazonS3Client.putObject(bucket, fileKey, file.getInputStream(), metadata);

        return fileUrl;
    }

    // 파일 수정 (기존 파일을 삭제하고 새 파일을 업로드)
    public String updateFile(Entity entity, MultipartFile newFile, String directoryName, String newFileName) throws IOException, SdkClientException, AmazonServiceException {
        // 기존 파일 메타데이터 조회
        // 기존 파일을 S3에서 삭제
        amazonS3Client.deleteObject(bucket, entity.getFileUrl());

        // 새로운 파일을 업로드
        String newFileUrl = uploadFile(newFile, directoryName, newFileName);

        return newFileUrl;
    }

    // 파일 삭제 (S3와 DB에서 파일 삭제)
    public void deleteFile(Entity entity) throws SdkClientException, AmazonServiceException {
        // S3에서 파일 삭제
        amazonS3Client.deleteObject(bucket, entity.getFileUrl());
    }
}
