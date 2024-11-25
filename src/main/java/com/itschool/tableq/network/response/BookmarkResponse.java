package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Bookmark;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookmarkResponse {
    private Long id;
    private RestaurantResponse restaurant;
    private UserResponse user;

    public static BookmarkResponse of(Bookmark bookmark){
        Restaurant restaurant = bookmark.getRestaurant();
        User user = bookmark.getUser();

        return BookmarkResponse.builder()
                .id(bookmark.getId())
                .restaurant(RestaurantResponse.of(restaurant))
                .user(UserResponse.builder()
                        .id(user.getId())
                        .build())
                .build();
    }
}
