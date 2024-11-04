package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<List<Reservation>> findByRestaurant(Restaurant restaurant);
    Optional<List<Reservation>> findByUser(User user);

    Optional<Long> countBy();
}
