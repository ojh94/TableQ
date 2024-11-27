package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BusinessInformationRequest;
import com.itschool.tableq.network.response.BusinessInformationResponse;
import com.itschool.tableq.service.BusinessInformationService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "사업자정보", description = "사업자정보 관련 API")
@RestController
@RequestMapping("/api/business-information")
public class BusinessInformationApiController extends CrudController<BusinessInformationRequest, BusinessInformationResponse, BusinessInformation> {

    @Override
    protected Class<BusinessInformationRequest> getRequestClass() {
        return BusinessInformationRequest.class;
    }

    @Operation(summary = "식당 관리자 별 사업자 번호 조회", description = "Owner ID 및 pageable로 엔티티 목록 조회")
    @GetMapping("/owner/{ownerId}")
    public Header<List<BusinessInformationResponse>> readByOwnerId(@PathVariable(name = "ownerId") Long ownerId,
                                                                   @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("{]", "{}", "{}", "read: ", ownerId, pageable);
        return ((BusinessInformationService) baseService).readByOwnerId(ownerId, pageable);
    }
}

