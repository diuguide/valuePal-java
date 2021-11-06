package com.example.valuepaljava.service;

import com.example.valuepaljava.models.Holding;
import com.example.valuepaljava.models.Order;
import com.example.valuepaljava.repos.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public String saveHolding(Order order) {
        System.out.println(order.toString());
        return "test";
    }



}
