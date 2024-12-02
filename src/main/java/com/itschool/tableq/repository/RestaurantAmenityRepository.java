package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Amenity;
import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantAmenityRepository extends JpaRepository<RestaurantAmenity, Long> {
    List<RestaurantAmenity> findByRestaurant(Restaurant restaurant);

    Optional<RestaurantAmenity> findByRestaurantAndAmenity(Restaurant restaurant, Amenity amenity);
}
