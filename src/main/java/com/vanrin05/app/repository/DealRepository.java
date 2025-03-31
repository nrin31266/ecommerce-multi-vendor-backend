package com.vanrin05.app.repository;

import com.vanrin05.app.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal, Long> {
}
