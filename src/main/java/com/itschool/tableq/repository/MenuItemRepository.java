package com.itschool.tableq.repository;

import com.itschool.tableq.domain.MenuItem;
import com.itschool.tableq.domain.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByRestaurant(Restaurant restaurant);

    Optional<MenuItem> getFirstByOrderByIdDesc();

    Optional<Long> countBy();
}
