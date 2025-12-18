package com.moneylog.ai.config;

import com.moneylog.ai.entity.AssetCategory;
import com.moneylog.ai.entity.AssetPosition;
import com.moneylog.ai.entity.Portfolio;
import com.moneylog.ai.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final PortfolioRepository portfolioRepository;

    @Override
    public void run(String... args) {
        if (portfolioRepository.count() > 0) {
            log.info("Skipping demo data initialization, existing portfolios found: {}", portfolioRepository.count());
            return;
        }

        log.info("Initializing demo data for MoneyLog AI...");

        // 2025-01
        Portfolio jan = createPortfolio("2025-01",
                new Object[][]{
                        {"中证500指数A", AssetCategory.AH_Stock, 120_000d, 3_200d, 18_000d},
                        {"标普500指数", AssetCategory.US_Stock, 80_000d, 2_800d, 15_500d},
                        {"黄金ETF", AssetCategory.Commodity, 50_000d, 900d, 6_000d},
                        {"短债基金A", AssetCategory.Bond, 60_000d, 400d, 2_300d},
                        {"货币基金", AssetCategory.Cash, 30_000d, 150d, 800d}
                });

        // 2025-02
        Portfolio feb = createPortfolio("2025-02",
                new Object[][]{
                        {"中证500指数A", AssetCategory.AH_Stock, 125_000d, 1_800d, 19_800d},
                        {"标普500指数", AssetCategory.US_Stock, 82_000d, -500d, 15_000d},
                        {"黄金ETF", AssetCategory.Commodity, 52_000d, 600d, 6_600d},
                        {"短债基金A", AssetCategory.Bond, 61_000d, 320d, 2_620d},
                        {"货币基金", AssetCategory.Cash, 32_000d, 160d, 960d}
                });

        portfolioRepository.save(jan);
        portfolioRepository.save(feb);

        log.info("Demo data initialization completed. Created {} portfolios", portfolioRepository.count());
    }

    private Portfolio createPortfolio(String month, Object[][] positionsData) {
        Portfolio portfolio = new Portfolio();
        portfolio.setDate(month);
        portfolio.setAiSummary(null);

        for (Object[] row : positionsData) {
            AssetPosition p = new AssetPosition();
            p.setName((String) row[0]);
            p.setCategory((AssetCategory) row[1]);
            p.setAmount((Double) row[2]);
            p.setMonthlyGain((Double) row[3]);
            p.setTotalGain((Double) row[4]);
            p.setPortfolio(portfolio);
            portfolio.getPositions().add(p);
        }

        return portfolio;
    }
}


