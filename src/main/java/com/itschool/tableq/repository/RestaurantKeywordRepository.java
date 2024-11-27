package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.RestaurantKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantKeywordRepository extends JpaRepository<RestaurantKeyword, Long>{
    List<RestaurantKeyword> findByRestaurant(Restaurant restaurant);
}
