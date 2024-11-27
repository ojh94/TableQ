package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Owner;
import com.itschool.tableq.network.response.OwnerResponse;
import com.itschool.tableq.network.request.OwnerRequest;
import com.itschool.tableq.service.base.BaseService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "점주", description = "점주 관련 API") // 문서에서 쉽게 찾도록 한글로 했음
@RestController
@RequestMapping("/api/owner")
public class OwnerApiController extends CrudController<OwnerRequest, OwnerResponse, Owner> {

    // 생성자
    @Autowired
    public OwnerApiController(BaseService<OwnerRequest, OwnerResponse, Owner> baseService) {
        super(baseService);
    }

    @Override
    protected Class<OwnerRequest> getRequestClass() {
        return OwnerRequest.class;
    }

    // 사용자 정보를   if새로 설정하기 위한 추가 작업
}
