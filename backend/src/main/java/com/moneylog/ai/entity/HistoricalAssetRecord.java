package com.moneylog.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historical_asset_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalAssetRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 7)
    private String month; // YYYY-MM format
    
    @Column(nullable = false)
    private String assetName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetCategory category;
    
    @Column(nullable = false)
    private Double amount; // Current Market Value
    
    @Column(nullable = false)
    private Double monthlyGain; // Gain this month
    
    @Column(nullable = false)
    private Double totalGain; // Total cumulative gain
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}