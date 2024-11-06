package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.domain.Restaurant;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class BreakHourResponse {
    private Long id;
    private LocalTime breakStart;
    private LocalTime breakEnd;
    private String dayOfWeek;
    private Restaurant restaurant;

    public BreakHourResponse(BreakHour breakHour) {
        this.id = breakHour.getId();
        this.breakStart = breakHour.getBreakStart();
        this.breakEnd = breakHour.getBreakEnd();
        this.dayOfWeek = breakHour.getDayOfWeek();
        this.restaurant = breakHour.getRestaurant();
    }
}
