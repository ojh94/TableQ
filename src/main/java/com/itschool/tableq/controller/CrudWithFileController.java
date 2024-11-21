package com.itschool.tableq.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.ifs.CrudWithFileInterface;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.base.FileRequest;
import com.itschool.tableq.service.base.BaseServiceWithS3;
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

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10 MB 제한

    protected abstract Class<Req> getRequestClass();

    private boolean isValidFileType(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
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

    @PostMapping("")
    public Header<Res> create(@RequestPart("data") String requestJson,  // JSON을 String으로 받기
                              @RequestPart("file") MultipartFile file) {

        log.info("create: {}", requestJson);

        // 제네릭 타입 Req에 맞게 요청 객체를 변환
        Req request;
        try {
            request = objectMapper.readValue(requestJson, getRequestClass());
        } catch (Exception e) {
            log.error("JSON 파싱 오류", e);
            return Header.ERROR("잘못된 JSON 형식입니다.");
        }

        // 파일 크기 체크
        if (file.getSize() > MAX_FILE_SIZE) return Header.ERROR("파일 크기가 제한을 초과합니다.");

        // 파일 유형 확인
        if (!isValidFileType(file)) return Header.ERROR("잘못된 파일 유형입니다.");

        try {
            request.setFile(file);  // 요청 객체에 파일 설정
            return baseService.create(Header.OK(request));  // 서비스 레이어로 넘기기
        } catch (Exception e) {
            log.error("파일 업로드 처리 중 오류 발생", e);
            return Header.ERROR("파일 처리에 실패했습니다.");
        }
    }


    @Override
    @Operation(summary = "수정", description = "ID로 엔티티를 업데이트")
    @PutMapping("{id}")
    public Header<Res> update(@PathVariable(name = "id") Long id,
                              @RequestPart("data") String requestJson,  // String으로 JSON 받기
                              @RequestPart("file") MultipartFile file) {
        log.info("update: {}, {}", id, requestJson);

        // requestJson을 Req 객체로 변환
        Req request;
        try {
            request = objectMapper.readValue(requestJson, (Class<Req>) FileRequest.class);  // JSON 문자열을 객체로 변환
        } catch (Exception e) {
            log.error("JSON 파싱 오류", e);
            return Header.ERROR("잘못된 JSON 형식입니다.");
        }

        // 파일 크기 체크
        if (file.getSize() > MAX_FILE_SIZE) {
            return Header.ERROR("파일 크기가 제한을 초과합니다.");
        }

        // 파일 유형 확인
        if (!isValidFileType(file)) return Header.ERROR("잘못된 파일 유형입니다.");

        try {
            request.setFile(file);  // FileRequest의 setFile 메서드로 파일 설정
            return baseService.update(id, Header.OK(request));  // 서비스 레이어로 넘기기
        } catch (Exception e) {
            log.error("파일 수정 중 오류 발생", e);
            return Header.ERROR("파일 처리에 실패했습니다.");
        }
    }

    @Override
    @DeleteMapping("{id}")
    @Operation(summary = "삭제", description = "ID로 엔티티를 삭제")
    public Header delete(@PathVariable(name = "id") Long id) {
        log.info("{}","{}","delete: ",id);
        baseService.delete(id);
        return Header.OK();
    }
}