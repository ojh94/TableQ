package com.itschool.tableq.network.request.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestWithFile extends SingleKeyRequest {
    protected MultipartFile file;

    // protected boolean needFileChange;
}
