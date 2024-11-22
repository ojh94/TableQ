package com.itschool.tableq.service.base;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public abstract class BaseServiceWithS3<Req, Res, Entity extends IncludeFileUrl> extends BaseService<Req, Res, Entity>{
    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    private String generateS3Key(MultipartFile file, String directoryName, String newFileName) {
        return directoryName + "/" + newFileName;
    }

    private String generateS3Url(String fileKey) {
        return "https://" + bucketName + ".s3.amazonaws.com/" + fileKey;
    }

    private String extractFileKey(String fileUrl) {
        String prefix = "https://" + bucketName + ".s3.amazonaws.com/";
        return fileUrl.replace(prefix, "");
    }

    private String extractDirectoryNameFromS3Url(String fileUrl) {
        String fileKey = extractFileKey(fileUrl);
        int lastSlashIndex = fileKey.lastIndexOf('/');
        return (lastSlashIndex != -1) ? fileKey.substring(0, lastSlashIndex) : "";
    }

    private String extractFileNameFromS3Url(String fileUrl) {
        String fileKey = extractFileKey(fileUrl);
        int lastSlashIndex = fileKey.lastIndexOf('/');
        return (lastSlashIndex != -1) ? fileKey.substring(lastSlashIndex + 1) : fileKey;
    }

    public String uploadFile(MultipartFile file, String directoryName, String newFileName) throws IOException, SdkClientException, AmazonServiceException {

        // 디렉토리 경로 포함한 S3 객체 키 설정
        String fileKey = generateS3Key(file, directoryName, newFileName);

        // S3 URL 생성
        String s3Url = generateS3Url(fileKey);

        // 메타데이터 설정
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        // S3에 파일 업로드
        if(!amazonS3Client.doesObjectExist(bucketName, s3Url)) {
            amazonS3Client.putObject(bucketName, fileKey, file.getInputStream(), metadata);
        } else {
            throw new RuntimeException("S3에 같은 파일 키가 존재합니다.");
        }

        return s3Url;
    }

    // 파일 수정 (기존 파일을 삭제하고 새 파일을 업로드)
    public String updateFile(String s3Url, MultipartFile newFile) throws IOException, SdkClientException, AmazonServiceException {

        String directoryName = extractDirectoryNameFromS3Url(s3Url);
        String newFileName = extractFileNameFromS3Url(s3Url);

        // 대상 파일 존재 여부
        boolean isExist = amazonS3Client.doesObjectExist(bucketName, directoryName + "/" + newFileName);

        if(isExist) {
            // 기존 파일을 S3에서 삭제
            deleteFile(s3Url);
        }
        // 기존 파일 메타데이터 조회

        // 새로운 파일을 업로드
        return uploadFile(newFile, directoryName, newFileName);
    }

    // 파일 삭제 (S3와 DB에서 파일 삭제)
    public void deleteFile(String s3Url) throws SdkClientException, AmazonServiceException {
        // S3에서 파일 삭제
        amazonS3Client.deleteObject(bucketName, s3Url);
    }
}
