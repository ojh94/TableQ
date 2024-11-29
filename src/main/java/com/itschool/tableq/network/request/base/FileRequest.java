package com.itschool.tableq.network.request.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FileRequest extends SingleKeyRequest {
    protected MultipartFile file;

    protected boolean needFileChange;
}
