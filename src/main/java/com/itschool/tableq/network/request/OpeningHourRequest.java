package com.itschool.tableq.network.request;

import com.itschool.tableq.network.request.base.SingleKeyRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
@Setter
public class OpeningHourRequest extends SingleKeyRequest {

    private Long id;

    private LocalTime openAt;

    private LocalTime closeAt;

    private DayOfWeek dayOfWeek;

    private RestaurantRequest restaurant;
}
