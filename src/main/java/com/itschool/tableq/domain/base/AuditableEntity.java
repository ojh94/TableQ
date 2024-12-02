package com.itschool.tableq.domain.base;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class AuditableEntity<Req extends SingleKeyRequest> extends SingleKeyEntity<Long, Req> {

    @CreatedDate
    @Column(updatable = false, nullable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    // nullable = false
    // not null 조건을 주려면 DB에서 updated_at 컬럼에 default값을 현재 시간으로 설정 필요
    protected LocalDateTime lastModifiedAt;

    @CreatedBy
    @Column(updatable = false, nullable = false)
    protected String createdBy;

    @LastModifiedBy
    @Column(insertable = false)
    protected String lastModifiedBy;
}