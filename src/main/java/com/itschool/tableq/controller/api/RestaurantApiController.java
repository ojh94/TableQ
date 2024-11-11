package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.service.RestaurantService;
import groovy.util.logging.Slf4j;
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

    @GetMapping("/search")
    public List<Restaurant> searchRestaurants(@RequestParam("arg0") String keyword) {
        return restaurantService.searchByName(keyword);
    }

}
