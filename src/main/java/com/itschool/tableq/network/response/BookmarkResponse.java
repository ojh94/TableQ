package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.Bookmark;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BookmarkResponse {
    private Long id;
    private Long restaurant_id;
    private Long user_id;

    public BookmarkResponse(Bookmark bookmark){
        this.id = bookmark.getId();
        this.restaurant_id = bookmark.getRestaurant().getId();
        this.user_id = bookmark.getUser().getId();
    }
}
