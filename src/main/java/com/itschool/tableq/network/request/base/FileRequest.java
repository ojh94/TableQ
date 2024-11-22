package com.itschool.tableq.network.request.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class FileRequest {
    protected MultipartFile file;

    protected boolean needFileChange = false;
}
