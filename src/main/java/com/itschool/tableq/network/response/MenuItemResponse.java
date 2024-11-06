package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuItemResponse {

    private Long id;

    private String name;

    private String price;

    private String description;

    @URL
    private String imageUrl;

    private Boolean recommendation;

    public MenuItemResponse(MenuItem menuItem) {
        this.id = menuItem.getId();
        this.name = menuItem.getName();
        this.price = menuItem.getPrice();
        this.description = menuItem.getDescription();
        this.imageUrl = menuItem.getDescription();
        this.recommendation = menuItem.getRecommendation();
    }
}