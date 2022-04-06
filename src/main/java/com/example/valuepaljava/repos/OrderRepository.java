package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Set<Order> getOrdersByWalletId(Integer wallet_id);

    @Query(value="SELECT getAvgPurchasePrice(:ticker, :wallet_id)", nativeQuery = true)
    Double getAvgPurchasePrice(@Param("ticker") String ticker, @Param("wallet_id") int wallet_id);


}
