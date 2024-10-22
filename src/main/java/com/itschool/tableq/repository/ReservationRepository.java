package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservations, Long> {
}
