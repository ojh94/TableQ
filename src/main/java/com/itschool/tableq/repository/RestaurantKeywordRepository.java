package com.itschool.tableq.repository;

import com.itschool.tableq.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantKeywordRepository extends JpaRepository<RestaurantKeyword, Long>{
    List<RestaurantKeyword> findByRestaurant(Restaurant restaurant);

    Optional<RestaurantKeyword> findByRestaurantAndKeyword(Restaurant restaurant, Keyword keyword);
}
