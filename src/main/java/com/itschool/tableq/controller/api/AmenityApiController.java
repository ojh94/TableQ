package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Amenity;
import com.itschool.tableq.network.request.AmenityRequest;
import com.itschool.tableq.network.response.AmenityResponse;
import groovy.util.logging.Slf4j;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "편의시설", description = "편의시설 관련 API")
@RestController
@RequestMapping("/api/amenity")
public class AmenityApiController extends CrudController<AmenityRequest, AmenityResponse, Amenity> {
    @Override
    protected Class<AmenityRequest> getRequestClass() {
        return AmenityRequest.class;
    }
}
