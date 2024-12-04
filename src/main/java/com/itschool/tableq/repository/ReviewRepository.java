package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Reservation;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 레스토랑에 해당하는 리뷰를 페이지를 지원하여 조회
    Page<Review> findByRestaurant(Restaurant restaurant, Pageable pageable);

    Page<Review> findByUser(User user, Pageable pageable);

    @Query("SELECT r FROM Review r WHERE r.reservation.restaurant.id = :restaurantId")
    Page<Review> findByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

    @Query("SELECT r from Review r WHERE r.reservation.user.id = :userId")
    Page<Review> findByUserId(@Param("userId") Long userId, Pageable pageable);

    List<Review> findByUserAndRestaurantAndCreatedAtBetween(User user, Restaurant restaurant, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Review> findByReservation(Reservation reservation);

    Optional<Review> getFirstByOrderByIdDesc();

    Optional<Long> countBy();
}
