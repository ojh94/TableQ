package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.RestaurantAmenity;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantAmenityRequest;
import com.itschool.tableq.network.response.MenuItemResponse;
import com.itschool.tableq.network.response.RestaurantAmenityResponse;
import com.itschool.tableq.service.MenuItemService;
import com.itschool.tableq.service.RestaurantAmenityService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "점포 편의시설", description = "점포 편의시설 관련 API")
@RestController
@RequestMapping("/api/restaurantamenity")
public class RestaurantAmenityApiController extends CrudController<RestaurantAmenityRequest, RestaurantAmenityResponse, RestaurantAmenity> {

    @Override
    protected Class<RestaurantAmenityRequest> getRequestClass() {
        return null;
    }

    @Operation(summary = "레스토랑별 편의시설 조회", description = "Restaurant ID로 엔티티 목록을 조회")
    @GetMapping("/restaurant/{id}")
    public Header<List<RestaurantAmenityResponse>> readByRestaurantId(@PathVariable(name = "id") Long id){
        log.info("{}","{}","read: ",id);
        return ((RestaurantAmenityService)baseService).readByRestaurantId(id);
    }
}
