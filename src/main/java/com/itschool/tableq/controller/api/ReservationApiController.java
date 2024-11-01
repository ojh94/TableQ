package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.network.response.ReservationResponse;
import com.itschool.tableq.network.request.ReservationRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "예약", description = "예약 관련 API")
@RestController
@RequestMapping("api/reservation")
public class ReservationApiController extends
        CrudController<ReservationRequest, ReservationResponse, Reservation> {

}
