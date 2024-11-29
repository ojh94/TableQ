package com.itschool.tableq.domain.base;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import groovy.lang.DeprecationException;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

@MappedSuperclass
@Getter
public abstract class IncludeFileUrl<Req extends SingleKeyRequest> extends AuditableEntity<Req> {
    @Column
    protected String fileUrl;

    public abstract void updateWithoutFileUrl(Req requestEntity);

    @Override
    @Deprecated
    public void update(Req requestEntity) {
        updateWithoutFileUrl(requestEntity);
        throw new DeprecationException("파일도 업데이트해야 하므로 updateWithoutFileUrl(), updateFileUrl() 사용할 것");
    }

    public void updateFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
    }
}
