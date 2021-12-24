package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Set<Order> getOrdersByWalletId(Integer wallet_id);


}
