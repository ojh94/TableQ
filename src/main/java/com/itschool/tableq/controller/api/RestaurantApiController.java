package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.service.RestaurantService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "레스토랑 검색", description = "레스토랑 이름 일부분으로 검색가능")
    @GetMapping("/keywordSearch")
    public List<RestaurantResponse> searchRestaurantsByName(@RequestParam("arg0") String keyword) {
        return restaurantService.searchByName(keyword);
    }

    @Operation(summary = "레스토랑 지역 검색", description = "레스토랑 이름 일부분으로 검색가능")
    @GetMapping("/addressSearch")
    public List<RestaurantResponse> searchByRestaurantsAddress(@RequestParam("arg0") String keyword) {
        return restaurantService.searchByAddress(keyword);
    }

}
