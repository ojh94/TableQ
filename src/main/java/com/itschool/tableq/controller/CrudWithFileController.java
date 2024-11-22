package com.itschool.tableq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.ifs.CrudWithFileInterface;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.base.FileRequest;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import com.itschool.tableq.util.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Component
public abstract class CrudWithFileController<Req extends FileRequest, Res, Entity extends IncludeFileUrl> implements CrudWithFileInterface<Req, Res> {

    @Autowired(required = false)
    protected BaseServiceWithS3<Req, Res, Entity> baseService;

    @Autowired
    private ObjectMapper objectMapper;  // Jackson ObjectMapper를 사용하여 JSON을 객체로 변환

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final long MAX_IMAGE_FILE_SIZE = 10 * 1024 * 1024; // 10 MB 제한

    protected abstract Class<Req> getRequestClass();

    private Req validateRequest(String requestJson, MultipartFile file) {
        // 제네릭 타입 Req에 맞게 요청 객체를 변환

        // 파일 크기 체크
        if (!FileUtil.isValidFileSize(file, MAX_IMAGE_FILE_SIZE))
            throw new RuntimeException("파일 크기가 제한을 초과합니다.");

        // 파일 유형 확인
        if (!FileUtil.validateImageFileExtension(file))
            throw new RuntimeException("잘못된 파일 유형입니다. (jpg, jpeg, png 확장자만 허용)");

        Req request;
        try {
            request = objectMapper.readValue(requestJson, getRequestClass());
        } catch (Exception e) {
            log.error("JSON 파싱 오류", e);
            throw new RuntimeException("잘못된 JSON 형식입니다.");
        }

        return request;
    }

    @GetMapping("")
    @Operation(summary = "페이지별 조회", description = "pageable로 엔티티 목록을 조회")
    public Header getPaginatedList(@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("{}","{}","getPaginatedList: ", pageable);
        return baseService.getPaginatedList(pageable);
    }

    @Override
    @Operation(summary = "읽기", description = "ID로 엔티티를 조회")
    @GetMapping("{id}")
    public Header<Res> read(@PathVariable(name = "id") Long id){
        log.info("{}","{}","read: ",id);
        return baseService.read(id);
    }

    @Operation(summary = "파일 및 엔티티 생성", description = "새로운 엔티티를 생성하고 파일을 S3에 저장")
    @PostMapping("")
    public Header<Res> create(@RequestPart("data") String requestJson,  // JSON을 String으로 받기
                              @RequestPart("file") MultipartFile file) {

        log.info("create: {}", requestJson);

        try {
            Req request = validateRequest(requestJson, file);

            request.setFile(file);  // 요청 객체에 파일 설정
            return baseService.create(Header.OK(request));  // 서비스 레이어로 넘기기
        } catch (Exception e) {
            log.error("파일 업로드 처리 중 오류 발생", e);
            return Header.ERROR("Create 실패 : " + e.getMessage());
        }
    }


    @Override
    @Operation(summary = "파일 및 엔티티 수정", description = "엔티티 수정 및 S3에 저장된 파일 수정")
    @PutMapping("{id}")
    public Header<Res> update(@PathVariable(name = "id") Long id,
                              @RequestPart("data") String requestJson,  // String으로 JSON 받기
                              @RequestPart("file") MultipartFile file) {
        log.info("update: {}, {}", id, requestJson);

        try {
            Req request = validateRequest(requestJson, file);

            request.setFile(file);  // FileRequest의 setFile 메서드로 파일 설정
            return baseService.update(id, Header.OK(request));  // 서비스 레이어로 넘기기
        } catch (Exception e) {
            log.error("파일 수정 중 오류 발생", e);
            return Header.ERROR("Update 실패 : " + e.getMessage());
        }
    }

    @Override
    @DeleteMapping("{id}")
    @Operation(summary = "삭제", description = "ID로 엔티티를 삭제 및 S3에 저장된 파일도 같이 삭제")
    public Header delete(@PathVariable(name = "id") Long id) {
        try {
            log.info("{}","{}","delete: ",id);
            baseService.delete(id);
            return Header.OK();
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생", e);
            return Header.ERROR("delete 실패 : " + e.getMessage());
        }
    }

    // 예외 처리 핸들러 추가
    /*@ExceptionHandler(Exception.class)
    public Header handleException(Exception e) {
        log.error("예기치 않은 오류 발생: ", e);
        return Header.ERROR("예기치 않은 오류가 발생했습니다. 500 : Internal Server Error");
    }*/
}