package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.Review;
import com.itschool.tableq.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<List<Review>> findByRestaurant(Restaurant restaurant);
    Optional<List<Review>> findByUser(User user);
}
