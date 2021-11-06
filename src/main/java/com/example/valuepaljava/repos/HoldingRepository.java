package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface HoldingRepository extends JpaRepository<Holding, Integer> {

    Optional<Holding> findHoldingByTicker(String Ticker);
    Set<Holding> findHoldingByWallet_WalletId(int walletId);
}
