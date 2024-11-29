package com.itschool.tableq.service.base;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.base.RequestWithFile;
import com.itschool.tableq.util.FileUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
public abstract class BaseServiceWithS3<Req extends RequestWithFile, Res, Entity extends IncludeFileUrl> extends BaseService<Req, Res, Entity>{

    protected final S3Service s3Service;

    protected abstract String getDirectoryNameOfS3Bucket();

    // 생성자
    @Autowired
    public BaseServiceWithS3(JpaRepository<Entity, Long> baseRepository, S3Service s3Service) {
        super(baseRepository);
        this.s3Service = s3Service;
    }

    @Transactional
    @Override
    public Header<Res> create(Header<Req> request) {
        try {
            Req requestEntity = request.getData();

            Entity entity = convertBaseEntityFromRequest(requestEntity);

            entity = getBaseRepository().save(entity);

            String fileUrl = s3Service.uploadFile(requestEntity.getFile(), getDirectoryNameOfS3Bucket(),
                    entity.getId() + FileUtil.getFileExtension(request.getData().getFile()));

            entity.updateFileUrl(fileUrl);

            Res menuItemResponse = response(entity);

            return Header.OK(menuItemResponse);
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 create 메소드 실패", e);
        }
    }


    @Transactional
    @Override
    public Header<Res> update(Long id, Header<Req> request) {
        try {
            Req menuItemRequest = request.getData();

            MultipartFile file = menuItemRequest.getFile();

            Entity findEntity = getBaseRepository().findById(id)
                    .orElseThrow(() -> new EntityNotFoundException());

            String existingUrl = findEntity.getFileUrl();

            if (menuItemRequest.isNeedFileChange()) { // 프론트에서 파일 변경이 필요하다 한 경우
                if (file.isEmpty() && existingUrl != null) { // 대체 파일이 없고 기존 url이 있는 경우
                    s3Service.deleteFile(existingUrl); // 기존 파일 삭제
                    findEntity.updateFileUrl(null); // 파일 URL 삭제
                } else if (!file.isEmpty()) { // 대체 파일이 있는 경우
                    String newUrl = s3Service.updateFile(existingUrl, file); // 새 파일 업로드
                    findEntity.updateFileUrl(newUrl); // 새 파일 URL 설정
                }
            }

            findEntity.updateWithoutFileUrl(menuItemRequest);

            return Header.OK(response(findEntity));
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 update 메소드 실패", e);
        }
    }

    @Transactional
    @Override
    public Header delete(Long id) {
        try {
            Entity entity = getBaseRepository().findById(id)
                    .orElseThrow(() -> new EntityNotFoundException());

            if(entity.getFileUrl() != null)
                s3Service.deleteFile(entity.getFileUrl());

            getBaseRepository().delete(entity);

            return Header.OK(response(entity));
        } catch (Exception e){
            throw new RuntimeException(this.getClass() + "의 delete 메소드 실패", e);
        }
    }
}
