package com.moneylog.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PortfolioDTO {
    private Long id;
    private String date;
    private List<AssetPositionDTO> positions;
    private String aiSummary;
}

