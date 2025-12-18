package com.moneylog.ai.service;

import com.moneylog.ai.dto.*;
import com.moneylog.ai.entity.AssetPosition;
import com.moneylog.ai.entity.Portfolio;
import com.moneylog.ai.repository.AssetPositionRepository;
import com.moneylog.ai.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PortfolioService {
    
    private final PortfolioRepository portfolioRepository;
    private final AssetPositionRepository assetPositionRepository;
    private final GeminiService geminiService;
    
    @Transactional(readOnly = true)
    public PortfolioDTO getPortfolioByDate(String date) {
        Portfolio portfolio = portfolioRepository.findByDate(date)
                .orElse(null);
        
        if (portfolio == null) {
            return new PortfolioDTO(null, date, List.of(), null);
        }
        
        return convertToDTO(portfolio);
    }
    
    @Transactional
    public PortfolioDTO savePortfolio(PortfolioDTO dto) {
        Portfolio portfolio = portfolioRepository.findByDate(dto.getDate())
                .orElse(new Portfolio());
        
        portfolio.setDate(dto.getDate());
        portfolio.setAiSummary(dto.getAiSummary());
        
        // Clear existing positions
        portfolio.getPositions().clear();
        
        // Add new positions
        if (dto.getPositions() != null) {
            for (AssetPositionDTO posDto : dto.getPositions()) {
                AssetPosition position = new AssetPosition();
                position.setName(posDto.getName());
                position.setCategory(posDto.getCategory());
                position.setAmount(posDto.getAmount());
                position.setMonthlyGain(posDto.getMonthlyGain());
                position.setTotalGain(posDto.getTotalGain());
                position.setPortfolio(portfolio);
                portfolio.getPositions().add(position);
            }
        }
        
        Portfolio saved = portfolioRepository.save(portfolio);
        return convertToDTO(saved);
    }
    
    @Transactional
    public AssetPositionDTO addPosition(String date, AssetPositionDTO dto) {
        Portfolio portfolio = portfolioRepository.findByDate(date)
                .orElseGet(() -> {
                    Portfolio p = new Portfolio();
                    p.setDate(date);
                    return portfolioRepository.save(p);
                });
        
        AssetPosition position = new AssetPosition();
        position.setName(dto.getName());
        position.setCategory(dto.getCategory());
        position.setAmount(dto.getAmount());
        position.setMonthlyGain(dto.getMonthlyGain());
        position.setTotalGain(dto.getTotalGain());
        position.setPortfolio(portfolio);
        
        AssetPosition saved = assetPositionRepository.save(position);
        return convertToDTO(saved);
    }
    
    @Transactional
    public AssetPositionDTO updatePosition(String date, Long positionId, AssetPositionDTO dto) {
        AssetPosition position = assetPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        
        // Verify it belongs to the correct portfolio
        if (!position.getPortfolio().getDate().equals(date)) {
            throw new RuntimeException("Position does not belong to the specified portfolio");
        }
        
        position.setName(dto.getName());
        position.setCategory(dto.getCategory());
        position.setAmount(dto.getAmount());
        position.setMonthlyGain(dto.getMonthlyGain());
        position.setTotalGain(dto.getTotalGain());
        
        AssetPosition saved = assetPositionRepository.save(position);
        return convertToDTO(saved);
    }
    
    @Transactional
    public void deletePosition(String date, Long positionId) {
        AssetPosition position = assetPositionRepository.findById(positionId)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        
        // Verify it belongs to the correct portfolio
        if (!position.getPortfolio().getDate().equals(date)) {
            throw new RuntimeException("Position does not belong to the specified portfolio");
        }
        
        assetPositionRepository.delete(position);
    }
    
    @Transactional
    public String generateAiSummary(String date) {
        Portfolio portfolio = portfolioRepository.findByDate(date)
                .orElseThrow(() -> new RuntimeException("Portfolio not found for date: " + date));
        
        List<AssetPositionDTO> positions = portfolio.getPositions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        String summary = geminiService.generateFinancialDiary(date, positions);
        
        portfolio.setAiSummary(summary);
        portfolioRepository.save(portfolio);
        
        return summary;
    }
    
    @Transactional(readOnly = true)
    public List<HistoryRecordDTO> getHistory() {
        List<Portfolio> portfolios = portfolioRepository.findAll();
        
        return portfolios.stream()
                .map(p -> {
                    double totalAssets = p.getPositions().stream()
                            .mapToDouble(AssetPosition::getAmount)
                            .sum();
                    double totalGain = p.getPositions().stream()
                            .mapToDouble(AssetPosition::getMonthlyGain)
                            .sum();
                    
                    return new HistoryRecordDTO(p.getDate(), totalAssets, totalGain);
                })
                .sorted((a, b) -> a.getMonth().compareTo(b.getMonth()))
                .collect(Collectors.toList());
    }
    
    private PortfolioDTO convertToDTO(Portfolio portfolio) {
        List<AssetPositionDTO> positions = portfolio.getPositions().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PortfolioDTO(
                portfolio.getId(),
                portfolio.getDate(),
                positions,
                portfolio.getAiSummary()
        );
    }
    
    private AssetPositionDTO convertToDTO(AssetPosition position) {
        return new AssetPositionDTO(
                position.getId(),
                position.getName(),
                position.getCategory(),
                position.getAmount(),
                position.getMonthlyGain(),
                position.getTotalGain()
        );
    }
}

