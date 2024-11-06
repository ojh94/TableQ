package com.itschool.tableq.repository;

import com.itschool.tableq.domain.RestaurantKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantKeywordsRepository extends JpaRepository<RestaurantKeyword, Long>{
}
