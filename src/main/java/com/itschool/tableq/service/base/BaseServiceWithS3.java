package com.itschool.tableq.service.base;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseServiceWithS3<Req, Res, Entity extends IncludeFileUrl> extends BaseService<Req, Res, Entity>{

    protected final S3Service s3Service;

    // 생성자
    @Autowired
    public BaseServiceWithS3(JpaRepository<Entity, Long> baseRepository, S3Service s3Service) {
        super(baseRepository);
        this.s3Service = s3Service;
    }
}
