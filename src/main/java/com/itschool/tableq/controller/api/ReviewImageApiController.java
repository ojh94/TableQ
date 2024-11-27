package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudWithFileController;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.request.ReviewImageRequest;
import com.itschool.tableq.network.response.ReviewImageResponse;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "리뷰 사진", description = "리뷰 사진 관련 API")
@RestController
@RequestMapping("/api/review-image")
public class ReviewImageApiController extends CrudWithFileController <ReviewImageRequest, ReviewImageResponse, ReviewImage> {

    @Override
    protected Class<ReviewImageRequest> getRequestClass() {
        return ReviewImageRequest.class;
    }
}
