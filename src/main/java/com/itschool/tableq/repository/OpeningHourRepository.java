package com.itschool.tableq.repository;

import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OpeningHourRepository extends JpaRepository<OpeningHour, Long> {

    Page<OpeningHour> findByRestaurant(Restaurant restaurant, Pageable pageable);

    List<OpeningHour> findAllByRestaurant(Restaurant restaurant);
}
