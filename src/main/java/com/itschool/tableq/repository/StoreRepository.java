package com.itschool.tableq.repository;

import com.itschool.tableq.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, String > {
}
