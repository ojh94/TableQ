package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.RestaurantKeyword;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import com.itschool.tableq.network.response.ReservationResponse;
import com.itschool.tableq.network.response.RestaurantKeywordResponse;
import com.itschool.tableq.service.ReservationService;
import com.itschool.tableq.service.RestaurantKeywordService;
import com.itschool.tableq.service.base.BaseService;
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
@Tag(name = "점포 키워드", description = "점포 관련 API")
@RestController
@RequestMapping("/api/restaurantkeyword")
public class RestaurantKeywordApiController extends CrudController<RestaurantKeywordRequest, RestaurantKeywordResponse, RestaurantKeyword>  {

    // 생성자
    @Autowired
    public RestaurantKeywordApiController(RestaurantKeywordService baseService) {
        super(baseService);
    }

    @Override
    protected Class<RestaurantKeywordRequest> getRequestClass() {
        return RestaurantKeywordRequest.class;
    }

    @Operation(summary = "레스토랑 키워드 조회", description = "Restaurant ID로 식당 관련 키워드 조회")
    @GetMapping("/restaurant/{restaurantId}")
    public Header<List<RestaurantKeywordResponse>> readByRestaurantId(@PathVariable(name="restaurantId") Long restaurantId
                                                                /*@PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable*/){
        log.info("{}","{}","read: ",restaurantId);
        return ((RestaurantKeywordService)baseService).readByRestaurantId(restaurantId);
    }
}
