package com.moneylog.ai.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "asset_positions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetPosition {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssetCategory category;
    
    @Column(nullable = false)
    private Double amount; // Current Market Value
    
    @Column(nullable = false)
    private Double monthlyGain; // Gain this month
    
    @Column(nullable = false)
    private Double totalGain; // Total cumulative gain
    
    @Column(nullable = false, length = 7)
    private String month; // YYYY-MM format
    

}