package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.RestaurantKeywordRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "restaurant_keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RestaurantKeyword extends AuditableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", updatable = false)
    private Keyword keyword;

    @Builder
    public RestaurantKeyword(Restaurant restaurant, Keyword keyword){
        this.restaurant = restaurant;
        this.keyword = keyword;
    }

    public void update(RestaurantKeywordRequest restaurantKeywordRequest) {
        this.restaurant = restaurantKeywordRequest.getRestaurant();
        this.keyword = restaurantKeywordRequest.getKeyword();
    }
}

