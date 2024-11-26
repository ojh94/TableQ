package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Restaurant;
import com.itschool.tableq.network.Header;
import com.itschool.tableq.network.response.RestaurantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> getFirstByOrderByIdDesc();

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Restaurant> searchByName(@Param("keyword") String keyword, Pageable pageable); // 이름 검색 (페이징 지원)

    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.address) LIKE LOWER(CONCAT('%', :address, '%'))")
    Page<Restaurant> searchByAddress(@Param("address") String address, Pageable pageable); // 주소 검색 (페이징 지원)

    @Query("")
    Header<List<RestaurantResponse>> findRestaurantsOrderByReservationCountDesc(Pageable pageable);

    @Query("")
    Header<List<RestaurantResponse>> findTopRatedRestaurants(Pageable pageable);

    Optional<Long> countBy();
}