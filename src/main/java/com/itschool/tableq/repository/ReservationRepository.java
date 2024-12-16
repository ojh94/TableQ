package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    Optional<List<Reservation>> findByRestaurant(Restaurant restaurant);

    Page<Reservation> findByUser(User user, Pageable pageable);

    Reservation findByIdAndIsEnteredAndCreatedAtBetween(Long reservationId, Boolean isEntered,
                                                                  LocalDateTime startTime, LocalDateTime endTime);
    // userId, RestaurantId, LocalDateTime
    List<Reservation> findByUserAndRestaurantAndCreatedAtBetween(User user, Restaurant restaurant,
                                                                 LocalDateTime startOfDay, LocalDateTime endOfDay);

    List<Reservation> findByIsEnteredAndUserAndRestaurantAndCreatedAtBetween(
            Boolean isEntered, User user, Restaurant restaurant, LocalDateTime startOfDay, LocalDateTime endOfDay
    );

    // 예약 후 나의 순서 조회를 위한 Repository
    List<Reservation> findByIsEnteredAndRestaurantAndCreatedAtBetweenOrderByIdAsc(
            Boolean isEntered, Restaurant restaurant, LocalDateTime startOfDay, LocalDateTime endOfDay
    );

    List<Reservation> findByIsEnteredAndUserAndCreatedAtBetween(
            Boolean isEntered, User user, LocalDateTime startOfDay, LocalDateTime endOfDay
    );

    // 예약 전 대기열 조회를 위한 countBy
    Integer countByRestaurantIdAndIsEnteredAndCreatedAtBetween(
            Long restaurantId, Boolean isEntered, LocalDateTime startOfDay, LocalDateTime endOfDay
    );

    Long countByRestaurantAndCreatedAtBetween(
            Restaurant restaurant, LocalDateTime startOfDay, LocalDateTime endOfDay
    );

    Optional<Reservation> getFirstByOrderByIdDesc();
}
