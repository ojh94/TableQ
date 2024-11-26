package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.domain.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OpeningHourResponse {
    private Long id;
    private LocalTime openAt;
    private LocalTime closeAt;
    private DayOfWeek dayOfWeek;
    private RestaurantResponse restaurant;

    public static OpeningHourResponse of(OpeningHour openingHour) {
        Restaurant restaurant = openingHour.getRestaurant();
        
        return OpeningHourResponse.builder()
                .id(openingHour.getId())
                .openAt(openingHour.getOpenAt())
                .closeAt(openingHour.getCloseAt())
                .dayOfWeek(openingHour.getDayOfWeek())
                .restaurant(RestaurantResponse.builder()
                        .id(restaurant.getId())
                        .build())
                .build();
    }
}
