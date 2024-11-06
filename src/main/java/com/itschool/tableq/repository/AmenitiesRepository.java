package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AmenitiesRepository extends JpaRepository<Amenity, Long> {
}
