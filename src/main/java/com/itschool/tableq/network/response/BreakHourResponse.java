package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BreakHourResponse {
    private Long id;
    private LocalTime breakStart;
    private LocalTime breakEnd;
    private String dayOfWeek;
    private RestaurantResponse restaurant;

    public static BreakHourResponse of(BreakHour breakHour) {
        Restaurant restaurant = breakHour.getRestaurant();

        return BreakHourResponse.builder()
                .id(breakHour.getId())
                .breakStart(breakHour.getBreakStart())
                .breakEnd(breakHour.getBreakEnd())
                .dayOfWeek(breakHour.getDayOfWeek())
                .restaurant(RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .build())
                .build();
    }
}
