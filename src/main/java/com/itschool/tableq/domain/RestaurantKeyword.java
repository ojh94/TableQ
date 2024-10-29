package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "restaurant_keywords")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RestaurantKeyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "keyword_id", updatable = false)
    private Keyword keyword;

    @Builder
    public RestaurantKeyword(Restaurant restaurant, Keyword keyword){
        this.restaurant = restaurant;
        this.keyword = keyword;
    }
}

