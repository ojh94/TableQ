package com.itschool.tableq.repository;

import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningHoursRepository extends JpaRepository<OpeningHour, Long> {

    Page<OpeningHour> findByRestaurant(Restaurant restaurant, Pageable pageable);
}
