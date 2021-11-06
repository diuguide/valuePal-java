package com.example.valuepaljava.service;

import com.example.valuepaljava.models.Holding;
import com.example.valuepaljava.models.Order;
import com.example.valuepaljava.models.Wallet;
import com.example.valuepaljava.repos.HoldingRepository;
import com.example.valuepaljava.repos.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final HoldingRepository holdingRepository;
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(HoldingRepository holdingRepository, WalletRepository walletRepository) {
        this.holdingRepository = holdingRepository;
        this.walletRepository = walletRepository;
    }

    public Holding saveHolding(Order order) {
        Set<Holding> holding = findHolding(order.getWalletId());
        Holding newHolding = new Holding();
        for(Holding hld : holding) {
            if(hld.getTicker().equals(order.getTicker())) {
                System.out.println("Ticker found");
            }
        }
        return holdingRepository.save(holdingBuilder(order));
    }

    public Holding holdingBuilder(Order order) {
        Wallet currentWallet = walletRetriever(order.getWalletId());
        Holding newHolding = new Holding();
        newHolding.setTicker(order.getTicker());
        newHolding.setPrice(order.getPrice());
        newHolding.setQuantity(order.getQuantity());
        newHolding.setTotalValue();
        newHolding.setWallet(currentWallet);
        return newHolding;
    }

    public Set<Holding> findHolding(int wallet_id) {
        return holdingRepository.findHoldingByWallet_WalletId(wallet_id);
    }

    public Wallet walletRetriever(int walletId) {
        return walletRepository.getById(walletId);
    }





}
