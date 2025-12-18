package com.moneylog.ai.repository;

import com.moneylog.ai.entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByDate(String date);
    boolean existsByDate(String date);
}

