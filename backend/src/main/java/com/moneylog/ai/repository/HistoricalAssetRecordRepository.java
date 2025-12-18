package com.moneylog.ai.repository;

import com.moneylog.ai.entity.HistoricalAssetRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricalAssetRecordRepository extends JpaRepository<HistoricalAssetRecord, Long> {
    List<HistoricalAssetRecord> findByMonthOrderByCategoryAsc(String month);
    boolean existsByMonth(String month);
}