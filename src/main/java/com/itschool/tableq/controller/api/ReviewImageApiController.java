package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudWithFileController;
import com.itschool.tableq.domain.ReviewImage;
import com.itschool.tableq.network.request.ReviewImageRequestWithFile;
import com.itschool.tableq.network.response.ReviewImageResponse;
import com.itschool.tableq.service.ReviewImageService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "리뷰 사진", description = "리뷰 사진 관련 API")
@RestController
@RequestMapping("/api/review-image")
public class ReviewImageApiController extends CrudWithFileController <ReviewImageRequestWithFile, ReviewImageResponse, ReviewImage> {

    // 생성자
    @Autowired
    public ReviewImageApiController(ReviewImageService baseService) {
        super(baseService);
    }

    @Override
    protected Class<ReviewImageRequestWithFile> getRequestClass() {
        return ReviewImageRequestWithFile.class;
    }
}
