package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.HoldingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface HoldingRecordRepository extends JpaRepository<HoldingRecord, Integer> {

    @Query(value="SELECT * from createHoldingTable(:ticker, :wallet_id)", nativeQuery = true)
    Set<HoldingRecord> createHoldingRow(@Param("ticker") String ticker, @Param("wallet_id") int wallet_id);

}
