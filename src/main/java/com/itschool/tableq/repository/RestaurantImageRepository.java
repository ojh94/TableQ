package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantImageRepository extends JpaRepository<RestaurantImage, Long> {
    Optional<List<RestaurantImage>> findByRestaurant(Restaurant restaurant);

    Optional<RestaurantImage> getFirstByOrderByIdDesc();

    Optional<Long> countBy();
}
