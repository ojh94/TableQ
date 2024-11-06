package com.itschool.tableq.repository;

import com.itschool.tableq.domain.BuisnessInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessInformationsRepository extends JpaRepository<BuisnessInformation, Long> {
}
