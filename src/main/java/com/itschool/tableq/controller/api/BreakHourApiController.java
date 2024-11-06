package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.network.request.BreakHourRequest;
import com.itschool.tableq.network.response.BreakHourResponse;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "휴무시간", description = "휴무시간 관련 API")
@RestController
@RequestMapping("/api/breakhour")
public class BreakHourApiController extends CrudController<BreakHourRequest, BreakHourResponse, BreakHour> {
}
