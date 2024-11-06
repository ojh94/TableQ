package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.RestaurantKeyword;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import com.itschool.tableq.network.response.RestaurantKeywordResponse;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "점포 키워드", description = "점포 관련 API")
@RestController
@RequestMapping("/api/restaurantkeyword")
public class RestaurantKeywordApiController extends CrudController<RestaurantKeywordRequest, RestaurantKeywordResponse, RestaurantKeyword>  {
}
