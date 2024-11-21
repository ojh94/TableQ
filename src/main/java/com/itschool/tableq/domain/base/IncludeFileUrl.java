package com.itschool.tableq.domain.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class IncludeFileUrl extends AuditableEntity{
    @Column
    protected String fileUrl;
}
