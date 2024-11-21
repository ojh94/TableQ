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

    public String uploadFile(MultipartFile file, String directoryName, String newFileName) throws IOException, SdkClientException, AmazonServiceException{
        String fileUrl = "https://" + bucket + "/" +directoryName + "/" + newFileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        amazonS3Client.putObject(bucket,newFileName,file.getInputStream(), metadata);

        return fileUrl;
    }
}
