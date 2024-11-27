package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    // 레스토랑에 해당하는 리뷰를 페이지를 지원하여 조회
    Page<Review> findByRestaurant(Restaurant restaurant, Pageable pageable);

    Page<Review> findByUser(User user, Pageable pageable);

    List<Review> findByUserAndRestaurantAndCreatedAtBetween(User user, Restaurant restaurant, LocalDateTime startOfDay, LocalDateTime endOfDay);

    Optional<Review> getFirstByOrderByIdDesc();

    Optional<Long> countBy();
}
