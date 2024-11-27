package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.OpeningHourRequest;
import com.itschool.tableq.network.response.OpeningHourResponse;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.service.OpeningHourService;
import com.itschool.tableq.service.ReviewService;
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
@Tag(name = "운영시간", description = "운영시간 관련 API")
@RestController
@RequestMapping("/api/openinghour")
public class OpeningHourApiController extends CrudController<OpeningHourRequest, OpeningHourResponse, OpeningHour> {
    @Override
    protected Class<OpeningHourRequest> getRequestClass() {
        return OpeningHourRequest.class;
    }

    @Operation(summary = "레스토랑별 운영시간 조회", description = "Restaurant ID 및 pageable로 엔티티 목록을 조회")
    @GetMapping("/restaurant/{id}")
    public Header<List<OpeningHourResponse>> readByRestaurantId(@PathVariable(name = "id") Long id,
                                                           @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("{}","{}","{}", "read: ", id, pageable);
        return ((OpeningHourService)baseService).readByRestaurantId(id, pageable);
    }
}
