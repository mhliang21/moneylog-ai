package com.moneylog.ai.controller;

import com.moneylog.ai.dto.AssetPositionDTO;
import com.moneylog.ai.dto.HistoryRecordDTO;
import com.moneylog.ai.service.AssetHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class AssetHistoryController {

    private final AssetHistoryService assetHistoryService;

    /*
     * 获取指定月份的资产持仓
     * @param month
     * @return
     */
    @GetMapping("/{month}")
    public ResponseEntity<List<AssetPositionDTO>> getPositionsByMonth(@PathVariable String month) {
        List<AssetPositionDTO> positions = assetHistoryService.getPositionsByMonth(month);
        return ResponseEntity.ok(positions);
    }

    /*
     * 保存资产持仓
     * @param month
     * @param dto
     * @return
     */
    @PostMapping("/{month}")
    public ResponseEntity<AssetPositionDTO> savePosition(
            @PathVariable String month,
            @RequestBody AssetPositionDTO dto) {
        AssetPositionDTO saved = assetHistoryService.savePosition(month, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /*
     * 更新资产持仓
     * @param month
     * @param positionId
     * @param dto
     * @return
     */
    @PutMapping("/{month}/{positionId}")
    public ResponseEntity<AssetPositionDTO> updatePosition(
            @PathVariable String month,
            @PathVariable Long positionId,
            @RequestBody AssetPositionDTO dto) {
        AssetPositionDTO updated = assetHistoryService.updatePosition(month, positionId, dto);
        return ResponseEntity.ok(updated);
    }

    /*
     * 删除资产持仓
     * @param month
     * @param positionId
     * @return
     */
    @DeleteMapping("/{month}/{positionId}")
    public ResponseEntity<Void> deletePosition(
            @PathVariable String month,
            @PathVariable Long positionId) {
        assetHistoryService.deletePosition(month, positionId);
        return ResponseEntity.noContent().build();
    }

    /*
     * 获取历史记录
     * @return
     */
    @GetMapping
    public ResponseEntity<List<HistoryRecordDTO>> getHistory() {
        List<HistoryRecordDTO> history = assetHistoryService.getHistory();
        return ResponseEntity.ok(history);
    }
}