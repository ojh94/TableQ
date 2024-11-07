package com.itschool.tableq.repository;

import com.itschool.tableq.domain.BusinessInformation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessInformationRepository extends JpaRepository<BusinessInformation, Long> {

    Optional<Long> countBy();
    Optional<BusinessInformation> findByBusinessNumber(String businessNumber);
}
