package com.itschool.tableq.repository;

import com.itschool.tableq.domain.BuisnessInformation;
import com.itschool.tableq.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BuisnessInformationRepository extends JpaRepository<BuisnessInformation, Long> {

    Optional<Long> countBy();
}
