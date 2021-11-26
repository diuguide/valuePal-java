package com.example.valuepaljava.service;

import com.example.valuepaljava.models.Holding;
import com.example.valuepaljava.models.HoldingsUpdateDTO;
import com.example.valuepaljava.models.Wallet;
import com.example.valuepaljava.repos.HoldingRepository;
import com.example.valuepaljava.repos.OrderRepository;
import com.example.valuepaljava.repos.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RefreshService {

    private final Logger logger = LoggerFactory.getLogger(RefreshService.class);
    private final HoldingRepository holdingRepository;
    private final OrderRepository orderRepository;
    private final WalletRepository walletRepository;
    private final StockService stockService;

    @Autowired
    public RefreshService(HoldingRepository holdingRepository, OrderRepository orderRepository, WalletRepository walletRepository, StockService stockService) {
        this.holdingRepository = holdingRepository;
        this.orderRepository = orderRepository;
        this.walletRepository = walletRepository;
        this.stockService = stockService;
    }


    public String updateAllHoldings() {
        Set<String> allHoldings = holdingRepository.selectAllTickers();
        String[] holdingsAsStringArray = new String[allHoldings.size()];
        int index = 0;
        for(String str : allHoldings) {
            holdingsAsStringArray[index++] = str;
        }
        return stockService.getTickerData(2, holdingsAsStringArray);
    }

    @Transactional
    public void updateHoldingsTable(List<HoldingsUpdateDTO> holdings) {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        for(HoldingsUpdateDTO holding : holdings) {
            holdingRepository.updateTickerPrices(holding.getPrice(), holding.getChange(), holding.getTicker());
        }
        List<Holding> allHoldings = holdingRepository.findAll();
        for(Holding hld : allHoldings) {
            hld.setTotalValue();
            holdingRepository.updateTotalValue(hld.getTotalValue(), hld.getTicker(), hld.getWallet());
        }
        long endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        logger.info(String.format("[UPDATE] Updated holding information.  %s records updated in %s ms", allHoldings.size(), duration));
        updateAllWallets();
    }

    public void updateAllWallets() {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        List<Wallet> allWallets = walletRepository.findAll();
        for(Wallet wallet : allWallets) {
            Optional<Double> newTotal = holdingRepository.findTotalValue(wallet.getWalletId());
            newTotal.ifPresent(wallet::setTotalValue);
            walletRepository.save(wallet);
        }
        long endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        logger.info(String.format("[UPDATE] Updated wallets.  %s records updated in %s ms", allWallets.size(), duration));
    }
}
