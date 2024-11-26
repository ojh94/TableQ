package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.service.RestaurantService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "레스토랑", description = "레스토랑 관련 API")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantApiController extends CrudController<RestaurantRequest, RestaurantResponse, Restaurant> {

    @Autowired
    private RestaurantService restaurantService;

    // 예약 완료 건 수
    @Operation(summary = "인기순 레스토랑 목록 조회", description = "예약 완료된 건이 많은 순으로 레스토랑 목록을 조회")
    @GetMapping("/popular/restaurants")
    public Header<List<RestaurantResponse>> findRestaurantsOrderByReservationCountDesc(@Parameter(name = "pageable", description = "페이징 설정 (page, size)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10\n" + "}")
                                                                                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return restaurantService.findRestaurantsOrderByReservationCountDesc(pageable);
    }

    // 추천 순 : 리뷰 건별 별점 총합
    @Operation(summary = "추천순 레스토랑 목록 조회", description = "평균 평점이 높은 순으로 레스토랑 목록을 조회")
    @GetMapping("/recommend/restaurants")
    public Header<List<RestaurantResponse>> findTopRatedRestaurants(@Parameter(name = "pageable", description = "페이징 설정 (page, size)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10\n" + "}")
                                                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return restaurantService.findTopRatedRestaurants(pageable);
    }

    @Operation(summary = "레스토랑 검색", description = "레스토랑 이름 일부분으로 검색가능")
    @GetMapping("/keyword/search")
    public Header<List<RestaurantResponse>> searchRestaurantsByName(@RequestParam("name") String keyword,
                                                                    @Parameter(name = "pageable", description = "페이징 설정 (page, size, sort)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"sort\": [\"id,asc\"]\n"+ "}")
                                                                    @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return restaurantService.searchByName(keyword, pageable);
    }

    @Operation(summary = "레스토랑 지역 검색", description = "레스토랑 지역 일부분으로 검색가능")
    @GetMapping("/address/search")
    public Header<List<RestaurantResponse>> searchByRestaurantsAddress(@RequestParam("address") String keyword,
                                                                       @Parameter(name = "pageable", description = "페이징 설정 (page, size, sort)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"sort\": [\"id,asc\"]\n"+ "}")
                                                                       @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return restaurantService.searchByAddress(keyword, pageable);
    }

}
