package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Wallet getToTalCashByWalletId(int wallet_id);



}
