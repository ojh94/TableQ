package com.itschool.tableq.network.response;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.network.response.base.FileResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MenuItemResponse extends FileResponse {

    private Long id;

    private String name;

    private String price;

    private String description;

    private Boolean recommendation;

    public MenuItemResponse(MenuItem menuItem) {
        this.id = menuItem.getId();
        this.name = menuItem.getName();
        this.price = menuItem.getPrice();
        this.description = menuItem.getDescription();
        this.fileUrl = menuItem.getFileUrl();
        this.recommendation = menuItem.getRecommendation();
    }
}