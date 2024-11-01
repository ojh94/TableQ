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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "리뷰 ", description = "리뷰 관련 API")
@RestController
@RequestMapping("/api/review")
public class ReviewController extends CrudController<ReviewRequest, ReviewResponse, Review> {
    @Operation(summary = "레스토랑별 리뷰 조회", description = "Restaurant ID로 엔티티 목록을 조회")
    @GetMapping("/restaurant/{id}")
    public Header<List<ReviewResponse>> readByRestaurantId(@PathVariable(name = "id") Long id){
        log.info("{}","{}","read: ",id);
        return ((ReviewService)baseService).readByRestaurantId(id);
    }
}
