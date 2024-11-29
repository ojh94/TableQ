package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.network.request.MenuItemRequestWithFile;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "menu_items")
public class MenuItem extends IncludeFileUrl<MenuItemRequestWithFile> {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    private String description;

    private Boolean recommendation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Override
    public void updateWithoutFileUrl(MenuItemRequestWithFile requestEntity) {
        this.name = requestEntity.getName();
        this.price = requestEntity.getPrice();
        this.description = requestEntity.getDescription();
        this.recommendation = requestEntity.getRecommendation();
    }
}
