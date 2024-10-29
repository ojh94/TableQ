package com.itschool.tableq.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "restaurant_amenities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RestaurantAmenity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false,nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", updatable = false)
    private Restaurant restaurant;

    @ManyToOne
    @JoinColumn(name = "amenity_id",updatable = false)
    private Amenity amenity;

    @Builder
    public RestaurantAmenity(Restaurant restaurant, Amenity amenity){
        this.restaurant = restaurant;
        this.amenity = amenity;
    }
}
