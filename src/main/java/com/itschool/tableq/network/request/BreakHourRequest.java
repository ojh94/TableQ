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
public class BreakHourRequest extends SingleKeyRequest {

    private LocalTime breakStart;

    private LocalTime breakEnd;

    private DayOfWeek dayOfWeek;

    private RestaurantRequest restaurant;
}
