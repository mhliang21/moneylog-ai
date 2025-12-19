package com.moneylog.ai.dto;

import com.moneylog.ai.entity.AssetCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoricalAssetRecordDTO {
    private Long id;
    private String month;
    private String name;
    private AssetCategory category;
    private Double amount;
    private Double monthlyGain;
    private Double totalGain;
}