package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.network.request.update.RestaurantUpdateAllRequest;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.service.RestaurantLogicService;
import com.itschool.tableq.service.RestaurantService;
import com.itschool.tableq.util.FileUtil;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Tag(name = "레스토랑", description = "레스토랑 관련 API")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantApiController extends CrudController<RestaurantRequest, RestaurantResponse, Restaurant> {

    // 생성자
    @Autowired
    public RestaurantApiController(RestaurantLogicService baseService) {
        super(baseService);
    }

    @Override
    protected Class<RestaurantRequest> getRequestClass() {
        return RestaurantRequest.class;
    }

    // 예약 완료 건 수
    @Operation(summary = "인기순 레스토랑 목록 조회", description = "예약 완료된 건이 많은 순으로 레스토랑 목록을 조회")
    @GetMapping("/popular/restaurants")
    public Header<List<RestaurantResponse>> findRestaurantsOrderByReservationCountDesc(@Parameter(name = "pageable", description = "페이징 설정 (page, size)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10\n" + "}")
                                                                                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ((RestaurantService)baseService).findRestaurantsOrderByReservationCountDesc(pageable);
    }

    // 추천 순 : 리뷰 건별 별점 총합
    @Operation(summary = "추천순 레스토랑 목록 조회", description = "평균 평점이 높은 순으로 레스토랑 목록을 조회")
    @GetMapping("/recommend/restaurants")
    public Header<List<RestaurantResponse>> findTopRatedRestaurants(@Parameter(name = "pageable", description = "페이징 설정 (page, size)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10\n" + "}")
                                                                    @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ((RestaurantService)baseService).findTopRatedRestaurants(pageable);
    }

    @Operation(summary = "레스토랑 검색", description = "레스토랑 이름 일부분으로 검색가능")
    @GetMapping("/keyword/search")
    public Header<List<RestaurantResponse>> searchRestaurantsByName(@RequestParam("name") String keyword,
                                                                    @Parameter(name = "pageable", description = "페이징 설정 (page, size, sort)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"sort\": [\"id,asc\"]\n"+ "}")
                                                                    @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ((RestaurantService)baseService).searchByName(keyword, pageable);
    }

    @Operation(summary = "레스토랑 지역 검색", description = "레스토랑 지역 일부분으로 검색가능")
    @GetMapping("/address/search")
    public Header<List<RestaurantResponse>> searchByRestaurantsAddress(@RequestParam("address") String keyword,
                                                                       @Parameter(name = "pageable", description = "페이징 설정 (page, size, sort)", example = "{\n" + "  \"page\": 0,\n" + "  \"size\": 10,\n" + "  \"sort\": [\"id,asc\"]\n"+ "}")
                                                                       @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ((RestaurantService)baseService).searchByAddress(keyword, pageable);
    }

    @Operation(summary = "레스토랑 관련 내역 전체 수정", description = "레스토랑 관련 전체 수정")
    @PutMapping("/all/{id}")
    public Header<RestaurantResponse> updateAll(
            @PathVariable(name = "id") Long id,
            @RequestPart("data") String requestJson,
            @RequestPart(value = "restaurantImages", required = false) List<MultipartFile> restaurantImages,
            @RequestPart(value = "menuImages", required = false) List<MultipartFile> menuItemImages) {

        log.info("Update All Request Received: id={}, requestJson={}, restaurantImages={}, menuImages={}",
                id, requestJson, restaurantImages, menuItemImages);

        // JSON 파싱 및 객체 변환
        @Valid
        RestaurantUpdateAllRequest request = parseRequestToJson(requestJson, RestaurantUpdateAllRequest.class);

        // 이미지 파일 검증
        FileUtil.validateImages(restaurantImages, MAX_IMAGE_FILE_SIZE);
        FileUtil.validateImages(menuItemImages, MAX_IMAGE_FILE_SIZE);

        // 파일을 객체로 옮기기
        if(restaurantImages != null && restaurantImages.size() > 0)
            FileUtil.saveFileListInObjectList(request.getRestaurantImageList(), restaurantImages);

        if(menuItemImages != null && menuItemImages.size() > 0)
            FileUtil.saveFileListInObjectList(request.getMenuItemList(), menuItemImages);

        // 업데이트 서비스 호출
        return ((RestaurantLogicService) baseService).updateAll(id, request);
    }

    @Operation(summary = "내가 운영중인 식당 조회", description = "User ID로 내가 운영중인 식당 조회")
    @GetMapping("/owner/my-restaurants/{userId}")
    public Header<List<RestaurantResponse>> readMyRestaurants(@PathVariable(name = "userId") Long userId){
        log.info("read: ",userId);
        return ((RestaurantService)baseService).readAllMyRestaurant(userId);
    }
}
