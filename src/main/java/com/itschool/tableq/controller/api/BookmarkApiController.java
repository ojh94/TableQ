package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Bookmark;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.request.BookmarkRequest;
import com.itschool.tableq.network.response.BookmarkResponse;
import com.itschool.tableq.service.BookmarkService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "즐겨찾기", description = "즐겨찾기 관련 API")
@RestController
@RequestMapping("/api/bookmark")
public class BookmarkApiController extends CrudController<BookmarkRequest, BookmarkResponse, Bookmark> {

    // 생성자
    @Autowired
    public BookmarkApiController(BookmarkService baseService) {
        super(baseService);
    }

    @Override
    protected Class<BookmarkRequest> getRequestClass() {
        return BookmarkRequest.class;
    }

    @Operation(summary = "유저의 즐겨찾기 조회", description = "유저가 즐겨찾기한 목록 조회")
    @GetMapping("/user/{userId}")
    public Header<List<BookmarkResponse>> readByUserId(@PathVariable(name = "userId")Long userId,
                                                       @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        log.info("{}","{}","{}","read: ",userId, pageable);
        return ((BookmarkService) baseService).readByUserId(userId,pageable);
    }

    @Operation(summary = "식당 즐겨찾기 수 조회", description = "식당을 즐겨찾기한 유저 수 조회")
    @GetMapping("/restaurant/{restaurantId}")
    public Header<Integer> countBookmarks(@PathVariable(name = "restaurantId")Long restaurantId){
        log.info("{}","{}","{}","read: ",restaurantId);
        return ((BookmarkService)baseService).countBookmarks(restaurantId);
    }
}
