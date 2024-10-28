package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Response.RestaurantResponse;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.repository.RestaurantRepository;
import com.itschool.tableq.service.RestaurantService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "레스토랑", description = "레스토랑 관련 API")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController extends CrudController<RestaurantRequest, RestaurantResponse, Restaurant> {


}
