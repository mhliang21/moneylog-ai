package com.moneylog.ai.repository;

import com.moneylog.ai.entity.AssetPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AssetPositionRepository extends JpaRepository<AssetPosition, Long> {
    List<AssetPosition> findByMonthOrderByCategoryAsc(String month);
    Optional<AssetPosition> findByIdAndMonth(Long id, String month);
    boolean existsByMonth(String month);
}