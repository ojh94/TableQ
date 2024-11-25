package com.itschool.tableq.controller.api;

import com.itschool.tableq.controller.CrudController;
import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.ReservationResponse;
import com.itschool.tableq.network.request.ReservationRequest;
import com.itschool.tableq.service.ReservationService;
import com.itschool.tableq.service.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "예약", description = "예약 관련 API")
@RestController
@RequestMapping("api/reservation")
public class ReservationApiController extends
        CrudController<ReservationRequest, ReservationResponse, Reservation> {
    @Operation(summary = "레스토랑 예약 조회", description = "Restaurant ID로 식당에 예약한 손님 목록을 조회")
    @GetMapping("/restaurant/{restaurantId}")
    public Header<List<ReservationResponse>> readByRestaurantId(@PathVariable(name="restaurantId") Long restaurantId,
                                                                @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        log.info("{}","{}","{}","read: ",restaurantId, pageable);
        return ((ReservationService)baseService).readByRestaurantId(restaurantId,pageable);
    }

    @Operation(summary = "레스토랑 대기 순서 조회", description = "Restaurant ID로 식당 현재 대기 순서 조회")
    @GetMapping("/restaurant/queue/{restaurantId}")
    public Header<Integer> readNowQueue(@PathVariable(name="restaurantId")Long restaurantId){
        log.info("{}","{}","{}","read: ",restaurantId);
        return ((ReservationService)baseService).getQueue(restaurantId);
    }

    @Operation(summary = "유저 예약 조회", description = "User ID로 손님이 예약했던 기록 조회")
    @GetMapping("/user/{userId}")
    public Header<List<ReservationResponse>> readByUserId(@PathVariable(name="userId") Long userId,
                                                                @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable){
        log.info("{}","{}","{}","read: ",userId, pageable);
        return ((ReservationService)baseService).readByUserId(userId,pageable);
    }

    @Operation(summary = "유저 순번 조회", description = "Restaurant ID 와 Reservation ID로 대기열 조회")
    @GetMapping("/user/{restaurantId}/{reservationId}")
    public Header<Integer> readMyQueue(@PathVariable(name = "restaurantId") Long restaurantId,
                                     @PathVariable(name = "reservationId") Long reservationId){
        log.info("{}","{}","{}","read: ",restaurantId, reservationId);
        try {
            return ((ReservationService)baseService).getUserQueue(restaurantId, reservationId);
        } catch (Exception e) {
            return Header.ERROR(e.getMessage());
        }
    }
}
