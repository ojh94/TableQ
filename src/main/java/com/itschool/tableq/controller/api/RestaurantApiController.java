package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.update.RestaurantUpdateAllRequest;
import com.itschool.tableq.network.response.RestaurantResponse;
import com.itschool.tableq.network.request.RestaurantRequest;
import com.itschool.tableq.service.RestaurantService;
import com.itschool.tableq.util.FileUtil;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Tag(name = "레스토랑", description = "레스토랑 관련 API")
@RestController
@RequestMapping("/api/restaurant")
public class RestaurantApiController extends CrudController<RestaurantRequest, RestaurantResponse, Restaurant> {

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

    // JSON 파싱 로직 분리
    private RestaurantUpdateAllRequest parseUpdateAllRequestToJson(String requestJson) {
        try {
            return objectMapper.readValue(requestJson, RestaurantUpdateAllRequest.class);
        } catch (Exception e) {
            log.error("JSON Parsing Error: {}", e.getMessage(), e);
            throw new RuntimeException("Invalid JSON format.");
        }
    }

    // 이미지 유효성 검사 로직 분리
    private void validateImages(List<MultipartFile> images, String imageType) {
        if (images != null && !images.isEmpty()) {
            try {
                FileUtil.validateFileList(images, MAX_IMAGE_FILE_SIZE);
            } catch (Exception e) {
                log.error("File validation failed for {}: {}", imageType, e.getMessage(), e);
                throw new RuntimeException(imageType + " validation failed: " + e.getMessage());
            }
        }
    }

    @Operation(summary = "레스토랑 관련 내역 전체 수정", description = "레스토랑 관련 전체 수정")
    @PutMapping("/all/{id}")
    public Header<RestaurantResponse> updateAll(
            @PathVariable(name = "id") Long id,
            @RequestPart("data") String requestJson,
            @RequestPart(value = "restaurantImages", required = false) List<MultipartFile> restaurantImages,
            @RequestPart(value = "menuImages", required = false) List<MultipartFile> menuImages) {

        log.info("Update All Request Received: id={}, requestJson={}, restaurantImages={}, menuImages={}",
                id, requestJson, restaurantImages, menuImages);

        try {
            // JSON 파싱 및 객체 변환
            RestaurantUpdateAllRequest request = parseUpdateAllRequestToJson(requestJson);

            // 이미지 파일 검증
            validateImages(restaurantImages, "Restaurant Images");
            validateImages(menuImages, "Menu Images");

            // 업데이트 서비스 호출
            return ((RestaurantService) baseService).updateAll(id, request, restaurantImages, menuImages);
        } catch (Exception e) {
            log.error("Error during entity update: {}", e.getMessage(), e);
            return Header.ERROR("Entity update error: " + e.getMessage());
        }
    }
}
