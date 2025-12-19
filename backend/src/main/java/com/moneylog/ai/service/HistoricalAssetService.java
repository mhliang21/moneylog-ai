package com.moneylog.ai.service;

import com.moneylog.ai.dto.HistoricalAssetRecordDTO;
import com.moneylog.ai.dto.HistoryRecordDTO;
import com.moneylog.ai.entity.HistoricalAssetRecord;
import com.moneylog.ai.repository.HistoricalAssetRecordRepository;
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
public class HistoricalAssetService {

    private final HistoricalAssetRecordRepository historicalAssetRecordRepository;

    @Transactional(readOnly = true)
    public List<HistoricalAssetRecordDTO> getRecordsByMonth(String month) {
        List<HistoricalAssetRecord> records = historicalAssetRecordRepository.findByMonthOrderByCategoryAsc(month);
        return records.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public HistoricalAssetRecordDTO saveRecord(String month, HistoricalAssetRecordDTO dto) {
        HistoricalAssetRecord record = new HistoricalAssetRecord();
        record.setMonth(month);
        record.setAssetName(dto.getName());
        record.setCategory(dto.getCategory());
        record.setAmount(dto.getAmount());
        record.setMonthlyGain(dto.getMonthlyGain());
        record.setTotalGain(dto.getTotalGain());

        HistoricalAssetRecord saved = historicalAssetRecordRepository.save(record);
        return convertToDTO(saved);
    }

    @Transactional
    public HistoricalAssetRecordDTO updateRecord(String month, Long recordId, HistoricalAssetRecordDTO dto) {
        HistoricalAssetRecord record = historicalAssetRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found for month: " + month + " and id: " + recordId));

        record.setAssetName(dto.getName());
        record.setCategory(dto.getCategory());
        record.setAmount(dto.getAmount());
        record.setMonthlyGain(dto.getMonthlyGain());
        record.setTotalGain(dto.getTotalGain());

        HistoricalAssetRecord saved = historicalAssetRecordRepository.save(record);
        return convertToDTO(saved);
    }

    @Transactional
    public void deleteRecord(String month, Long recordId) {
        HistoricalAssetRecord record = historicalAssetRecordRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Record not found for month: " + month + " and id: " + recordId));

        historicalAssetRecordRepository.delete(record);
    }

    @Transactional(readOnly = true)
    public List<HistoryRecordDTO> getHistory() {
        List<HistoricalAssetRecord> allRecords = historicalAssetRecordRepository.findAll();

        // Group by month and calculate totals
        return allRecords.stream()
                .collect(Collectors.groupingBy(HistoricalAssetRecord::getMonth))
                .entrySet()
                .stream()
                .map(entry -> {
                    String month = entry.getKey();
                    List<HistoricalAssetRecord> records = entry.getValue();
                    
                    double totalAssets = records.stream()
                            .mapToDouble(HistoricalAssetRecord::getAmount)
                            .sum();
                            
                    double totalGain = records.stream()
                            .mapToDouble(HistoricalAssetRecord::getMonthlyGain)
                            .sum();
                            
                    return new HistoryRecordDTO(month, totalAssets, totalGain);
                })
                .sorted(Comparator.comparing(HistoryRecordDTO::getMonth))
                .collect(Collectors.toList());
    }

    private HistoricalAssetRecordDTO convertToDTO(HistoricalAssetRecord record) {
        return new HistoricalAssetRecordDTO(
                record.getId(),
                record.getMonth(),
                record.getAssetName(),
                record.getCategory(),
                record.getAmount(),
                record.getMonthlyGain(),
                record.getTotalGain()
        );
    }
}