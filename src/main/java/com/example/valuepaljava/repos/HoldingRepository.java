package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Holding;
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

    @Modifying
    @Query(value = "UPDATE holdings SET price = :new_price, change=:change, process_flag='U' WHERE ticker = :ticker", nativeQuery = true)
    void updateTickerPrices(@Param("new_price") double new_price, @Param("change")double change, @Param("ticker")String ticker);

    @Modifying
    @Query(value = "UPDATE holdings SET total_value = :new_total_value WHERE ticker = :ticker AND wallet_id = :wallet_id", nativeQuery = true)
    void updateTotalValue(@Param("new_total_value") double new_total_value, @Param("ticker")String ticker, @Param("wallet_id")int wallet_id);

    @Query(value= "SELECT * FROM holdings WHERE ticker = :ticker and wallet_id = :wallet_id", nativeQuery = true)
    Optional<Holding> findHoldingByWalletAndAndTicker(@Param("ticker") String ticker, @Param("wallet_id") int wallet_id);

    @Query(value="SELECT updateavgprice1()", nativeQuery = true)
    Integer updateAvgPrice();





}
