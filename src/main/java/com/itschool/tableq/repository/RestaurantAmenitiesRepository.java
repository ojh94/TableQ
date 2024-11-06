package com.itschool.tableq.repository;

import com.itschool.tableq.domain.RestaurantAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantAmenitiesRepository extends JpaRepository<RestaurantAmenity, Long> {
}
