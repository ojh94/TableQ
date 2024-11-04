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
    @Operation(summary = "레스토랑별 리뷰 조회", description = "Restaurant ID 및 pageable로 엔티티 목록을 조회")
    @GetMapping("/restaurant/{id}")
    public Header<List<ReviewResponse>> readByRestaurantId(@PathVariable(name = "id") Long id,
                                                           @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        log.info("{}","{}","{}", "read: ", id, pageable);
        return ((ReviewService)baseService).readByRestaurantId(id, pageable);
    }
}
