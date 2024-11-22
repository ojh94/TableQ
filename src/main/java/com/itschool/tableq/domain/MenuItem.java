package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.IncludeFileUrl;
import com.itschool.tableq.network.request.MenuItemRequest;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "menu_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class MenuItem extends IncludeFileUrl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    private String description;

    private Boolean recommendation;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Builder
    public MenuItem(String name, String price, String description,
                    Boolean recommendation, String fileUrl, Restaurant restaurant){
        this.name = name;
        this.price = price;
        this.description = description;
        this.recommendation = recommendation;
        this.restaurant = restaurant;
        this.fileUrl = fileUrl;
    }

    public void updateWithoutFileUrl(MenuItemRequest menuItemRequest) {
        this.name = menuItemRequest.getName();
        this.price = menuItemRequest.getPrice();
        this.description = menuItemRequest.getDescription();
        // this.fileUrl = menuItemRequest.getFile().getName();
        this.recommendation = menuItemRequest.getRecommendation();
    }

    public void updateFileUrl(String fileUrl){
        this.fileUrl = fileUrl;
    }
}
