package com.itschool.tableq.domain.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@Getter
public abstract class IncludeFileUrl extends AuditableEntity {
    @Column
    protected String fileUrl;

    public void updateFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
    }
}
