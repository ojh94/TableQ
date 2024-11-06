package com.itschool.tableq.repository;

import com.itschool.tableq.domain.BreakHour;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BreakHoursRepository extends JpaRepository<BreakHour, Long> {
}
