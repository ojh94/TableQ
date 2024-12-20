package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Keyword;
import com.itschool.tableq.network.request.KeywordRequest;
import com.itschool.tableq.network.response.KeywordResponse;
import com.itschool.tableq.service.KeywordService;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "키워드", description = "키워드 관련 API")
@RestController
@RequestMapping("/api/keyword")
public class KeywordApiController extends CrudController<KeywordRequest, KeywordResponse, Keyword> {

    // 생성자
    @Autowired
    public KeywordApiController(KeywordService baseService) {
        super(baseService);
    }

    @Override
    protected Class<KeywordRequest> getRequestClass() {
        return KeywordRequest.class;
    }
}
