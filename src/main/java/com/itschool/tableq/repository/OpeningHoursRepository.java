package com.itschool.tableq.repository;

import com.itschool.tableq.domain.OpeningHour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpeningHoursRepository extends JpaRepository<OpeningHour, Long> {

}
