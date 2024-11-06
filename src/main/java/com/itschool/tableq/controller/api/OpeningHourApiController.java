package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.network.request.OpeningHourRequest;
import com.itschool.tableq.network.response.OpeningHourResponse;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "운영시간", description = "운영시간 관련 API")
@RestController
@RequestMapping("/api/openinghour")
public class OpeningHourApiController extends CrudController<OpeningHourRequest, OpeningHourResponse, OpeningHour> {
}
