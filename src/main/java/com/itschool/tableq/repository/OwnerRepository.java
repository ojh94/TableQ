package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    Optional<Owner> findByEmail(String email);

    Optional<Owner> getFirstByOrderByIdDesc();

    Optional<Long> countBy();
}
