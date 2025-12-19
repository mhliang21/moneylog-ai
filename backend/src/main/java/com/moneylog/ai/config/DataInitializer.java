package com.moneylog.ai.config;

import com.moneylog.ai.entity.AssetCategory;
import com.moneylog.ai.entity.AssetPosition;
import com.moneylog.ai.entity.HistoricalAssetRecord;
import com.moneylog.ai.repository.AssetPositionRepository;
import com.moneylog.ai.repository.HistoricalAssetRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AssetPositionRepository assetPositionRepository;
    private final HistoricalAssetRecordRepository historicalAssetRecordRepository;

    @Override
    public void run(String... args) {
        // If both tables already have data, skip entirely
        if (assetPositionRepository.count() > 0 && historicalAssetRecordRepository.count() > 0) {
            log.info("Skipping demo data initialization, existing data found in both tables");
            return;
        }

        log.info("Initializing demo data for MoneyLog AI (idempotent)...");

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
        LocalDate now = LocalDate.now();

        // Seed current month and two previous months
        String thisMonth = now.format(fmt);
        String prevMonth1 = now.minusMonths(1).format(fmt);
        String prevMonth2 = now.minusMonths(2).format(fmt);

        // For each month, ensure positions and historical records exist (avoid duplicates)
        ensureMonthData(thisMonth);
        ensureMonthData(prevMonth1);
        ensureMonthData(prevMonth2);

        log.info("Demo data initialization finished.");
    }

    private void ensureMonthData(String month) {
        // If asset_positions for this month is missing, create them
        if (!assetPositionRepository.existsByMonth(month)) {
            log.debug("Creating asset_positions for month: {}", month);
            createPositionForMonth(month, true);
        } else {
            log.debug("asset_positions already exist for month: {}", month);
        }

        // If historical_asset_records for this month is missing, create them
        if (!historicalAssetRecordRepository.existsByMonth(month)) {
            log.debug("Creating historical_asset_records for month: {}", month);
            createPositionForMonth(month, false);
        } else {
            log.debug("historical_asset_records already exist for month: {}", month);
        }
    }

    private void createPositionForMonth(String month, boolean createAssetPosition) {
        // Use the same demo dataset as before
        if (createAssetPosition) {
            // AssetPosition entries
            createPositionEntity(month, "中证500指数A", AssetCategory.AH_Stock, 120_000d, 3_200d, 18_000d, true);
            createPositionEntity(month, "标普500指数", AssetCategory.US_Stock, 80_000d, 2_800d, 15_500d, true);
            createPositionEntity(month, "黄金ETF", AssetCategory.Commodity, 50_000d, 900d, 6_000d, true);
            createPositionEntity(month, "短债基金A", AssetCategory.Bond, 60_000d, 400d, 2_300d, true);
            createPositionEntity(month, "余额宝", AssetCategory.Cash, 30_000d, 100d, 300d, true);
        } else {
            // HistoricalAssetRecord entries
            createPositionEntity(month, "中证500指数A", AssetCategory.AH_Stock, 120_000d, 3_200d, 18_000d, false);
            createPositionEntity(month, "标普500指数", AssetCategory.US_Stock, 80_000d, 2_800d, 15_500d, false);
            createPositionEntity(month, "黄金ETF", AssetCategory.Commodity, 50_000d, 900d, 6_000d, false);
            createPositionEntity(month, "短债基金A", AssetCategory.Bond, 60_000d, 400d, 2_300d, false);
            createPositionEntity(month, "余额宝", AssetCategory.Cash, 30_000d, 100d, 300d, false);
        }
    }

    private void createPositionForMonth(String month, String name, AssetCategory category, Double amount, Double monthlyGain, Double totalGain) {
        // Deprecated helper - kept for compatibility if referenced elsewhere
    }

    private void createPositionEntity(String month, String name, AssetCategory category,
                                      Double amount, Double monthlyGain, Double totalGain, boolean isAssetPosition) {
        if (isAssetPosition) {
            AssetPosition position = new AssetPosition();
            position.setMonth(month);
            position.setName(name);
            position.setCategory(category);
            position.setAmount(amount);
            position.setMonthlyGain(monthlyGain);
            position.setTotalGain(totalGain);
            assetPositionRepository.save(position);
        } else {
            HistoricalAssetRecord record = new HistoricalAssetRecord();
            record.setMonth(month);
            record.setAssetName(name);
            record.setCategory(category);
            record.setAmount(amount);
            record.setMonthlyGain(monthlyGain);
            record.setTotalGain(totalGain);
            historicalAssetRecordRepository.save(record);
        }
    }
}
