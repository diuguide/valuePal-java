package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface HoldingRepository extends JpaRepository<Holding, Integer> {

    Optional<Holding> findHoldingByTicker(String Ticker);
    Set<Holding> findHoldingByWalletId(int walletId);

    @Query(value = "SELECT sum(total_value) FROM holdings WHERE wallet_id = :wallet_id", nativeQuery = true)
    Optional<Double> findTotalValue(@Param("wallet_id") int wallet_id);



}
