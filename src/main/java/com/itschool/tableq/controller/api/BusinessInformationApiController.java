package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.BusinessInformation;
import com.itschool.tableq.network.request.BusinessInformationRequest;
import com.itschool.tableq.network.response.BusinessInformationResponse;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "사업자정보", description = "사업자정보 관련 API")
@RestController
@RequestMapping("/api/buisnessinformation")
public class BusinessInformationApiController extends CrudController<BusinessInformationRequest, BusinessInformationResponse, BusinessInformation> {
}

