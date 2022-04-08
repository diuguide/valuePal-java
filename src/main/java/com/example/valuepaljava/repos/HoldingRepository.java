package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Holding;
import com.example.valuepaljava.models.HoldingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;


@Repository
public interface HoldingRepository extends JpaRepository<Holding, Integer> {

    Optional<Holding> findHoldingByTicker(String Ticker);
    Set<Holding> findHoldingByWalletIdOrderByQuantityDesc(int walletId);

    @Query(value = "SELECT sum(total_value) FROM holdings WHERE wallet_id = :wallet_id", nativeQuery = true)
    Optional<Double> findTotalValue(@Param("wallet_id") int wallet_id);

    @Query(value = "SELECT DISTINCT(ticker) FROM holdings", nativeQuery = true)
    Set<String> selectAllTickers();

    @Query(value= "SELECT * FROM holdings WHERE ticker = :ticker and wallet_id = :wallet_id", nativeQuery = true)
    Optional<Holding> findHoldingByWalletAndAndTicker(@Param("ticker") String ticker, @Param("wallet_id") int wallet_id);

    @Query(value="SELECT updateavgprice1()", nativeQuery = true)
    Integer updateAvgPrice();

    @Query(value="SELECT createHoldingRow(:ticker, :wallet_id)", nativeQuery = true)
    String createHoldingRow(@Param("ticker") String ticker, @Param("wallet_id") int wallet_id);





}
