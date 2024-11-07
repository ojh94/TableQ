package com.itschool.tableq.repository;

import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreakHoursRepository extends JpaRepository<BreakHour, Long> {
    Page<BreakHour> findByRestaurant(Restaurant restaurant, Pageable pageable);
}
