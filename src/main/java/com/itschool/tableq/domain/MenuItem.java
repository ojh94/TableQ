package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.network.request.MenuItemRequest;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "menu_items")
public class MenuItem extends IncludeFileUrl {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    private String description;

    private Boolean recommendation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public void updateWithoutFileUrl(MenuItemRequest menuItemRequest) {
        this.name = menuItemRequest.getName();
        this.price = menuItemRequest.getPrice();
        this.description = menuItemRequest.getDescription();
        this.recommendation = menuItemRequest.getRecommendation();
    }
}
