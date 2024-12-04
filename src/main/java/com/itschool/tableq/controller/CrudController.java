package com.itschool.tableq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.ifs.CrudInterface;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.base.SingleKeyRequest;
import com.itschool.tableq.network.request.update.RestaurantUpdateAllRequest;
import com.itschool.tableq.service.base.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Component
public abstract class CrudController<Req extends SingleKeyRequest, Res, Entity extends AuditableEntity> implements CrudInterface<Req, Res> {

    protected final BaseService<Req, Res, Entity> baseService;

    @Autowired
    protected ObjectMapper objectMapper;  // 필요 시 Jackson ObjectMapper를 사용하여 JSON을 객체로 변환

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    protected static final long MAX_IMAGE_FILE_SIZE = 10 * 1024 * 1024; // 10 MB 제한


    protected CrudController(BaseService<Req, Res, Entity> baseService) {
        this.baseService = baseService;
    }

    protected abstract Class<Req> getRequestClass();

    @GetMapping("")
    @Operation(summary = "페이지별 조회", description = "pageable로 엔티티 목록을 조회")
    public Header getPaginatedList(@Parameter(name = "pageable", description = "페이징 설정 (page, size, sort)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"sort\": [\"id,asc\"]\n"+ "}")
                                   @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){

        log.info("{}","{}","getPaginatedList: ", pageable);

        return baseService.getPaginatedList(pageable);
    }

    @Override
    @Operation(summary = "생성", description = "새로운 엔티티를 생성")
    @PostMapping("")
    public Header<Res> create(@RequestBody @Valid Header<Req> request) {

        log.info("{}","{}", "create : ", request);

        return baseService.create(request);
    }

    @Override
    @Operation(summary = "읽기", description = "ID로 엔티티를 조회")
    @GetMapping("{id}")
    public Header<Res> read(@PathVariable(name = "id") Long id) {

        log.info("{}","{}","read: ",id);

        return baseService.read(id);
    }

    @Override
    @Operation(summary = "수정", description = "ID로 엔티티를 업데이트")
    @PutMapping("{id}")
    public Header<Res> update(@PathVariable(name = "id") Long id,
                              @RequestBody @Valid Header<Req> request) {

        log.info("{}","{}","{}", "update: ", id, request);

        return baseService.update(id, request);
    }

    @Override
    @DeleteMapping("{id}")
    @Operation(summary = "삭제", description = "ID로 엔티티를 삭제")
    public Header delete(@PathVariable(name = "id") Long id) {

        log.info("{}","{}","delete: ",id);

        baseService.delete(id);

        return Header.OK();
    }

    // 예외 처리 핸들러 추가
    @ExceptionHandler(Exception.class)
    public Header handleException(Exception e) {

        log.error(e.getClass().getSimpleName() + " : " + e.getMessage() + "\n" + e.getCause() + ExceptionUtils.getStackTrace(e));

        return Header.ERROR(e.getClass().getSimpleName() + " : " + e.getCause());
    }

    // JSON 파싱 로직 분리
    protected <T> T parseRequestToJson(String requestJson, Class<T> className) {
        try {
            return objectMapper.readValue(requestJson, className);
        } catch (Exception e) {
            throw new RuntimeException("Invalid JSON format.");
        }
    }
}