package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.RestaurantAmenity;
import com.itschool.tableq.network.request.RestaurantAmenityRequest;
import com.itschool.tableq.network.response.RestaurantAmenityResponse;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "점표편의시설", description = "점포 편의시설 관련 API")
@RestController
@RequestMapping("/api/restaurantamenity")
public class RestaurantAmenityApiController extends CrudController<RestaurantAmenityRequest, RestaurantAmenityResponse, RestaurantAmenity> {
}
