package com.itschool.tableq.domain;

import com.itschool.tableq.domain.base.AuditableEntity;
import com.itschool.tableq.network.request.RestaurantAmenityRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "restaurant_amenities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class RestaurantAmenity extends AuditableEntity {
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

    public void update(RestaurantAmenityRequest restaurantAmenityRequest) {
        this.restaurant = restaurantAmenityRequest.getRestaurant();
        this.amenity = restaurantAmenityRequest.getAmenity();
    }
}
