package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudWithFileController;
import com.itschool.tableq.domain.RestaurantImage;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantImageRequestWithFile;
import com.itschool.tableq.network.response.RestaurantImageResponse;
import com.itschool.tableq.service.RestaurantImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "식당 이미지", description = "식당 이미지 관련 API")
@RestController
@RequestMapping("api/restaurant-image")
public class RestaurantImageApiController extends CrudWithFileController<RestaurantImageRequestWithFile, RestaurantImageResponse, RestaurantImage> {

    // 생성자
    @Autowired
    public RestaurantImageApiController(RestaurantImageService baseService) {
        super(baseService);
    }

    @Override
    protected Class<RestaurantImageRequestWithFile> getRequestClass() {
        return RestaurantImageRequestWithFile.class;
    }

    @GetMapping("/restaurant/{restaurantId}")
    @Operation(summary = "레스토랑 id별 조회", description = "레스토랑 id로 엔티티 목록을 조회")
    public Header readByRestaurantId(@PathVariable(name = "restaurantId") Long restaurantId){
        log.info("{}","{}","getImageListByRestaurantId : ", restaurantId);

        return ((RestaurantImageService)baseService).readByRestaurantId(restaurantId);
    }
}
