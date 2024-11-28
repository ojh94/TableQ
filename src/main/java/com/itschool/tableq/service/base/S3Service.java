package com.itschool.tableq.service.base;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class S3Service {

    private String bucketName;

    private final AmazonS3Client amazonS3Client;

    // 생성자
    @Autowired
    public S3Service(@Value("${cloud.aws.s3.bucket}")String bucketName,
                     AmazonS3Client amazonS3Client) {
        this.bucketName = bucketName;
        this.amazonS3Client = amazonS3Client;
    }

    private String generateS3Key(String directoryName, String newFileName) {
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

    public String uploadFile(MultipartFile file, String directoryName, String newFileName) {
        try {
            String fileKey = generateS3Key(directoryName, newFileName);
            String s3Url = generateS3Url(fileKey);

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            if (!amazonS3Client.doesObjectExist(bucketName, fileKey)) {
                amazonS3Client.putObject(bucketName, fileKey, file.getInputStream(), metadata);
            } else {
                throw new RuntimeException("S3에 같은 파일 키가 존재합니다.");
            }

            return s3Url;
        } catch (AmazonServiceException e) {
            throw new RuntimeException("AWS S3에서 오류 발생: " + e.getMessage(), e);
        } catch (SdkClientException e) {
            throw new RuntimeException("AWS S3 클라이언트 오류 발생: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage(), e);
        }
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
        // 파일 키 추출
        String fileKey = extractFileKey(s3Url);

        // S3에서 파일 삭제
        amazonS3Client.deleteObject(bucketName, fileKey);
    }
}
