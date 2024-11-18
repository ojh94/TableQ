package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Bookmark;
import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByRestaurant(Restaurant restaurant);
    Optional<List<Bookmark>> findByUser(User user);
}
