package com.itschool.tableq.network.response.base;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class FileResponse {
    protected String fileUrl;
}
