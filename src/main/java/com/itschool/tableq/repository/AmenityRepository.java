package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AmenityRepository extends JpaRepository<Amenity, Long> {

    Optional<Long> countBy();
}
