package com.itschool.tableq.controller;

import com.itschool.tableq.ifs.CrudInterface;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.service.base.BaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
public abstract class CrudController<Req, Res, Entity> implements CrudInterface<Req, Res> {
    @Autowired(required = false)
    protected BaseService<Req, Res, Entity> baseService;

    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("")
    @Operation(summary = "페이지별 조회", description = "pageable로 엔티티 목록을 조회")
    public Header getPaginatedList(@Parameter(name = "pageable", description = "페이징 설정 (page, size, sort)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"sort\": [\"id,asc\"]\n"+ "}")
                                   @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("{}","{}","getPaginatedList: ", pageable);
        try {
            return baseService.getPaginatedList(pageable);
        } catch (Exception e) {
            log.error("페이지 목록 조회 중 오류 발생", e);
            return Header.ERROR("페이지 목록을 조회하는 중 오류가 발생했습니다.");
        }
    }

    @Override
    @Operation(summary = "생성", description = "새로운 엔티티를 생성")
    @PostMapping("")
    public Header<Res> create(@RequestBody Header<Req> request) {
        log.info("{}","{}", "create : ", request);
        try {
            return baseService.create(request);
        } catch (Exception e) {
            log.error("엔티티 생성 중 오류 발생", e);
            return Header.ERROR("새로운 엔티티를 생성하는 중 오류가 발생했습니다.");
        }
    }

    @Override
    @Operation(summary = "읽기", description = "ID로 엔티티를 조회")
    @GetMapping("{id}")
    public Header<Res> read(@PathVariable(name = "id") Long id) {
        log.info("{}","{}","read: ",id);
        try {
            return baseService.read(id);
        } catch (Exception e) {
            log.error("ID로 엔티티 조회 중 오류 발생: {}", id, e);
            return Header.ERROR("엔티티 조회 중 오류가 발생했습니다.");
        }
    }

    @Override
    @Operation(summary = "수정", description = "ID로 엔티티를 업데이트")
    @PutMapping("{id}")
    public Header<Res> update(@PathVariable(name = "id") Long id,
                              @RequestBody Header<Req> request) {
        log.info("{}","{}","update: ",id,request);
        try {
            return baseService.update(id, request);
        } catch (Exception e) {
            log.error("엔티티 업데이트 중 오류 발생: {}", id, e);
            return Header.ERROR("엔티티 업데이트 중 오류가 발생했습니다.");
        }
    }

    @Override
    @DeleteMapping("{id}")
    @Operation(summary = "삭제", description = "ID로 엔티티를 삭제")
    public Header delete(@PathVariable(name = "id") Long id) {
        log.info("{}","{}","delete: ",id);
        try {
            baseService.delete(id);
            return Header.OK();
        } catch (Exception e) {
            log.error("엔티티 삭제 중 오류 발생: {}", id, e);
            return Header.ERROR("엔티티 삭제 중 오류가 발생했습니다.");
        }
    }

    // 예외 처리 핸들러 추가
    /*@ExceptionHandler(Exception.class)
    public Header handleException(Exception e) {
        log.error("예기치 않은 오류 발생: ", e);
        return Header.ERROR("예기치 않은 오류가 발생했습니다. 500 : Internal Server Error");
    }*/
}