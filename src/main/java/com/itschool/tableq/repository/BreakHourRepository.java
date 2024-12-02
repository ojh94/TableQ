package com.itschool.tableq.repository;

import com.itschool.tableq.domain.BreakHour;
import com.itschool.tableq.domain.OpeningHour;
import com.itschool.tableq.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface BreakHourRepository extends JpaRepository<BreakHour, Long> {
    Page<BreakHour> findByRestaurant(Restaurant restaurant, Pageable pageable);

    List<BreakHour> findAllByRestaurant(Restaurant restaurant);

    Optional<BreakHour> findByRestaurantAndDayOfWeek(Restaurant restaurant, DayOfWeek dayOfWeek);
}
