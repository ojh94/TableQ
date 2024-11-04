package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    Optional<Long> countBy();
}
