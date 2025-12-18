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

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final AssetPositionRepository assetPositionRepository;
    private final HistoricalAssetRecordRepository historicalAssetRecordRepository;

    @Override
    public void run(String... args) {
        if (assetPositionRepository.count() > 0 || historicalAssetRecordRepository.count() > 0) {
            log.info("Skipping demo data initialization, existing data found");
            return;
        }

        log.info("Initializing demo data for MoneyLog AI...");

        // 2025-01
        createPosition("2025-01", "中证500指数A", AssetCategory.AH_Stock, 120_000d, 3_200d, 18_000d);
        createPosition("2025-01", "标普500指数", AssetCategory.US_Stock, 80_000d, 2_800d, 15_500d);
        createPosition("2025-01", "黄金ETF", AssetCategory.Commodity, 50_000d, 900d, 6_000d);
        createPosition("2025-01", "短债基金A", AssetCategory.Bond, 60_000d, 400d, 2_300d);
        createPosition("2025-01", "余额宝", AssetCategory.Cash, 30_000d, 100d, 300d);

        // 2025-02
        createPosition("2025-02", "中证500指数A", AssetCategory.AH_Stock, 125_000d, 5_000d, 23_000d);
        createPosition("2025-02", "标普500指数", AssetCategory.US_Stock, 82_000d, 2_000d, 17_500d);
        createPosition("2025-02", "黄金ETF", AssetCategory.Commodity, 48_000d, -2_000d, 4_000d);
        createPosition("2025-02", "短债基金A", AssetCategory.Bond, 60_500d, 500d, 2_800d);
        createPosition("2025-02", "余额宝", AssetCategory.Cash, 31_000d, 100d, 400d);

        log.info("Demo data initialized successfully!");
    }

    private void createPosition(String month, String name, AssetCategory category, 
                               Double amount, Double monthlyGain, Double totalGain) {
        // Save to asset_positions table
        AssetPosition position = new AssetPosition();
        position.setMonth(month);
        position.setName(name);
        position.setCategory(category);
        position.setAmount(amount);
        position.setMonthlyGain(monthlyGain);
        position.setTotalGain(totalGain);
        assetPositionRepository.save(position);
        
        // Save to historical_asset_records table
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


