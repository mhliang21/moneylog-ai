package com.moneylog.ai.controller;

import com.moneylog.ai.dto.*;
import com.moneylog.ai.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
@RequiredArgsConstructor
public class PortfolioController {
    
    private final PortfolioService portfolioService;
    
    @GetMapping("/{date}")
    public ResponseEntity<PortfolioDTO> getPortfolio(@PathVariable String date) {
        PortfolioDTO portfolio = portfolioService.getPortfolioByDate(date);
        return ResponseEntity.ok(portfolio);
    }
    
    @PostMapping
    public ResponseEntity<PortfolioDTO> savePortfolio(@RequestBody PortfolioDTO dto) {
        PortfolioDTO saved = portfolioService.savePortfolio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @PostMapping("/{date}/positions")
    public ResponseEntity<AssetPositionDTO> addPosition(
            @PathVariable String date,
            @RequestBody AssetPositionDTO dto) {
        AssetPositionDTO saved = portfolioService.addPosition(date, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    @PutMapping("/{date}/positions/{positionId}")
    public ResponseEntity<AssetPositionDTO> updatePosition(
            @PathVariable String date,
            @PathVariable Long positionId,
            @RequestBody AssetPositionDTO dto) {
        AssetPositionDTO updated = portfolioService.updatePosition(date, positionId, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{date}/positions/{positionId}")
    public ResponseEntity<Void> deletePosition(
            @PathVariable String date,
            @PathVariable Long positionId) {
        portfolioService.deletePosition(date, positionId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{date}/ai-summary")
    public ResponseEntity<String> generateAiSummary(@PathVariable String date) {
        String summary = portfolioService.generateAiSummary(date);
        return ResponseEntity.ok(summary);
    }
    
    @GetMapping("/history")
    public ResponseEntity<List<HistoryRecordDTO>> getHistory() {
        List<HistoryRecordDTO> history = portfolioService.getHistory();
        return ResponseEntity.ok(history);
    }
}

