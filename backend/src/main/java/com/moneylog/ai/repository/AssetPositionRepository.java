package com.moneylog.ai.repository;

import com.moneylog.ai.entity.AssetPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetPositionRepository extends JpaRepository<AssetPosition, Long> {
}

