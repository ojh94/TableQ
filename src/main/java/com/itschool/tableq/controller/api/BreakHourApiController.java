package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BreakHourRequest;
import com.itschool.tableq.network.response.BreakHourResponse;
import com.itschool.tableq.service.BreakHourService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "휴무시간", description = "휴무시간 관련 API")
@RestController
@RequestMapping("/api/breakhour")
public class BreakHourApiController extends CrudController<BreakHourRequest, BreakHourResponse, BreakHour> {

    // 생성자
    @Autowired
    public BreakHourApiController(BreakHourService baseService) {
        super(baseService);
    }

    @Override
    protected Class<BreakHourRequest> getRequestClass() {
        return BreakHourRequest.class;
    }

    @Operation(summary = "레스토랑별 휴무시간 조회", description = "Restaurant ID 및 pageable로 엔티티 목록을 조회")
    @GetMapping("/restaurant/{restaurantId}")
    public Header<List<BreakHourResponse>> readByRestaurantId(@PathVariable(name = "restaurantId") Long restaurantId,
                                                           @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("{}","{}","{}", "read: ", restaurantId, pageable);
        return ((BreakHourService)baseService).readByRestaurantId(restaurantId, pageable);
    }
}
