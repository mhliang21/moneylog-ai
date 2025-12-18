package com.moneylog.ai.service;

import com.moneylog.ai.dto.AssetPositionDTO;
import com.moneylog.ai.dto.HistoryRecordDTO;
import com.moneylog.ai.entity.AssetPosition;
import com.moneylog.ai.repository.AssetPositionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AssetHistoryService {

    private final AssetPositionRepository assetPositionRepository;

    @Transactional(readOnly = true)
    public List<AssetPositionDTO> getPositionsByMonth(String month) {
        List<AssetPosition> positions = assetPositionRepository.findByMonthOrderByCategoryAsc(month);
        return positions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public AssetPositionDTO savePosition(String month, AssetPositionDTO dto) {
        AssetPosition position = new AssetPosition();
        position.setName(dto.getName());
        position.setCategory(dto.getCategory());
        position.setAmount(dto.getAmount());
        position.setMonthlyGain(dto.getMonthlyGain());
        position.setTotalGain(dto.getTotalGain());
        position.setMonth(month);

        AssetPosition saved = assetPositionRepository.save(position);
        return convertToDTO(saved);
    }

    @Transactional
    public AssetPositionDTO updatePosition(String month, Long positionId, AssetPositionDTO dto) {
        AssetPosition position = assetPositionRepository.findByIdAndMonth(positionId, month)
                .orElseThrow(() -> new RuntimeException("Position not found for month: " + month + " and id: " + positionId));

        position.setName(dto.getName());
        position.setCategory(dto.getCategory());
        position.setAmount(dto.getAmount());
        position.setMonthlyGain(dto.getMonthlyGain());
        position.setTotalGain(dto.getTotalGain());

        AssetPosition saved = assetPositionRepository.save(position);
        return convertToDTO(saved);
    }

    @Transactional
    public void deletePosition(String month, Long positionId) {
        AssetPosition position = assetPositionRepository.findByIdAndMonth(positionId, month)
                .orElseThrow(() -> new RuntimeException("Position not found for month: " + month + " and id: " + positionId));

        assetPositionRepository.delete(position);
    }

    @Transactional(readOnly = true)
    public List<HistoryRecordDTO> getHistory() {
        List<AssetPosition> allPositions = assetPositionRepository.findAll();

        // Group by month and calculate totals
        return allPositions.stream()
                .collect(Collectors.groupingBy(AssetPosition::getMonth))
                .entrySet()
                .stream()
                .map(entry -> {
                    String month = entry.getKey();
                    List<AssetPosition> positions = entry.getValue();
                    
                    double totalAssets = positions.stream()
                            .mapToDouble(AssetPosition::getAmount)
                            .sum();
                            
                    double totalGain = positions.stream()
                            .mapToDouble(AssetPosition::getMonthlyGain)
                            .sum();
                            
                    return new HistoryRecordDTO(month, totalAssets, totalGain);
                })
                .sorted(Comparator.comparing(HistoryRecordDTO::getMonth))
                .collect(Collectors.toList());
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