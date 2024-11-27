package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.ReviewRequest;
import com.itschool.tableq.network.response.ReviewResponse;
import com.itschool.tableq.service.ReviewService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@Slf4j
@Tag(name = "리뷰 ", description = "리뷰 관련 API")
@RestController
@RequestMapping("/api/review")
public class ReviewApiController extends CrudController<ReviewRequest, ReviewResponse, Review> {

    @Override
    protected Class<ReviewRequest> getRequestClass() {
        return ReviewRequest.class;
    }

    @Operation(summary = "레스토랑별 리뷰 조회", description = "Restaurant ID 및 pageable로 엔티티 목록을 조회")
    @GetMapping("/restaurant/{restaurantId}")
    public Header<List<ReviewResponse>> readByRestaurantId(@PathVariable(name = "restaurantId") Long restaurantId,
                                                           @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("{}","{}","{}", "read: ", restaurantId, pageable);
        return ((ReviewService)baseService).readByRestaurantId(restaurantId, pageable);
    }

    @Operation(summary = "사용자별 리뷰 조회", description = "User ID 및 pageable로 엔티티 목록을 조회")
    @GetMapping("/user/{userId}")
    public Header<List<ReviewResponse>> readByUserId(@PathVariable(name = "userId") Long UserId,
                                                     @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("{}","{}","{}", "read: ", UserId, pageable);
        return ((ReviewService)baseService).readByUserId(UserId, pageable);
    }

    @Operation(summary = "3일간 사용자 리뷰 개수 조회", description = "User ID 및 Restaurant ID로 리뷰 개수 조회")
    @GetMapping("/count/{userId}/{restaurantId}")
    public Header<Long> userReviewCount(@PathVariable(name = "userId") Long userId,
                                        @PathVariable(name= "restaurantId") Long restaurantId){
        log.info("read: ",userId, restaurantId);
        return ((ReviewService)baseService).countUserReviewFor3Days(restaurantId,userId);
    }
}
