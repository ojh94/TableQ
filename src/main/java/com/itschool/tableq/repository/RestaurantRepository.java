package com.itschool.tableq.repository;

import com.itschool.tableq.network.request.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}