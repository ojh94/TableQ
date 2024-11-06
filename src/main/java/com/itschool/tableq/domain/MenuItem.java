package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.MenuItemRequest;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "menu_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@Entity
public class MenuItem extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String price;

    private String description;

    private String imageUrl;

    private Boolean recommendation;

    @ManyToOne
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public void update(MenuItemRequest menuItemRequest) {
        this.name = menuItemRequest.getName();
        this.price = menuItemRequest.getPrice();
        this.description = menuItemRequest.getDescription();
        this.imageUrl = menuItemRequest.getImageUrl();
        this.recommendation = menuItemRequest.getRecommendation();
    }
}
