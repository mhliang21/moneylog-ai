package com.moneylog.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecordDTO {
    private String month;
    private Double totalAssets;
    private Double totalGain;
}

