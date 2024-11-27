package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudWithFileController;
import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.request.RestaurantImageRequest;
import com.itschool.tableq.network.response.RestaurantImageResponse;
import com.itschool.tableq.service.RestaurantImageService;
import com.itschool.tableq.service.base.BaseServiceWithS3;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "식당 이미지", description = "식당 이미지 관련 API")
@RestController
@RequestMapping("api/restaurant-image")
public class RestaurantImageApiController extends CrudWithFileController<RestaurantImageRequest, RestaurantImageResponse, RestaurantImage> {

    // 생성자
    @Autowired
    public RestaurantImageApiController(RestaurantImageService baseService) {
        super(baseService);
    }

    @Override
    protected Class<RestaurantImageRequest> getRequestClass() {
        return RestaurantImageRequest.class;
    }
}
