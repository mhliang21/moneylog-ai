package com.moneylog.ai.controller;

import com.moneylog.ai.dto.HistoricalAssetRecordDTO;
import com.moneylog.ai.dto.HistoryRecordDTO;
import com.moneylog.ai.service.HistoricalAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historical-assets")
@RequiredArgsConstructor
public class HistoricalAssetController {

    private final HistoricalAssetService historicalAssetService;

    /*
     * 获取指定月份的资产记录
     * @param month
     * @return
     */
    @GetMapping("/{month}")
    public ResponseEntity<List<HistoricalAssetRecordDTO>> getRecordsByMonth(@PathVariable String month) {
        List<HistoricalAssetRecordDTO> records = historicalAssetService.getRecordsByMonth(month);
        return ResponseEntity.ok(records);
    }

    /*
     * 保存资产记录
     * @param month
     * @param dto
     * @return
     */
    @PostMapping("/{month}")
    public ResponseEntity<HistoricalAssetRecordDTO> saveRecord(
            @PathVariable String month,
            @RequestBody HistoricalAssetRecordDTO dto) {
        HistoricalAssetRecordDTO saved = historicalAssetService.saveRecord(month, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /*
     * 更新资产记录
     * @param month
     * @param recordId
     * @param dto
     * @return
     */
    @PutMapping("/{month}/{recordId}")
    public ResponseEntity<HistoricalAssetRecordDTO> updateRecord(
            @PathVariable String month,
            @PathVariable Long recordId,
            @RequestBody HistoricalAssetRecordDTO dto) {
        HistoricalAssetRecordDTO updated = historicalAssetService.updateRecord(month, recordId, dto);
        return ResponseEntity.ok(updated);
    }

    /*
     * 删除资产记录
     * @param month
     * @param recordId
     * @return
     */
    @DeleteMapping("/{month}/{recordId}")
    public ResponseEntity<Void> deleteRecord(
            @PathVariable String month,
            @PathVariable Long recordId) {
        historicalAssetService.deleteRecord(month, recordId);
        return ResponseEntity.noContent().build();
    }

    /*
     * 获取历史记录
     * @return
     */
    @GetMapping("/history")
    public ResponseEntity<List<HistoryRecordDTO>> getHistory() {
        List<HistoryRecordDTO> history = historicalAssetService.getHistory();
        return ResponseEntity.ok(history);
    }
}