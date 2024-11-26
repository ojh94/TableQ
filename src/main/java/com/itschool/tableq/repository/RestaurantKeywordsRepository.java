package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantKeywordsRepository extends JpaRepository<RestaurantKeyword, Long>{
    List<RestaurantKeyword> findByRestaurant(Restaurant restaurant);
}
