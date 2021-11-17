package com.example.valuepaljava.service;

import com.example.valuepaljava.exceptions.InsufficientFundsException;
import com.example.valuepaljava.models.*;
import com.example.valuepaljava.repos.HoldingRepository;
import com.example.valuepaljava.repos.OrderRepository;
import com.example.valuepaljava.repos.UserRepository;
import com.example.valuepaljava.repos.WalletRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final HoldingRepository holdingRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StockService stockService;

    @Autowired
    public WalletService(HoldingRepository holdingRepository, WalletRepository walletRepository, UserRepository userRepository, OrderRepository orderRepository, StockService stockService) {
        this.holdingRepository = holdingRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.stockService = stockService;
    }

    public Order entryPoint(Order order, String token) {
        order.setWalletId(jwtUtility(token).getWallet().getWalletId());
        if(checkExistingBalance(order)) {
            saveHolding(order);
            updateWallet(order);
            order.setStatus("FILLED");
            orderRepository.save(order);
            logger.info(String.format("Wallet %s updated", order.getWalletId()));
            return order;
        }
        order.setStatus("INSUFFICIENT FUNDS");
        orderRepository.save(order);
        throw new InsufficientFundsException(String.format("Insufficient funds! $%s is needed to complete this order.", order.getTotalValue()));

    }

    public Wallet entryWallet(String token) {
        int walletId = jwtUtility(token.replace("Bearer ", "")).getWallet().getWalletId();
        Set<Holding> holdings = holdingRepository.findHoldingByWalletId(walletId);
        Wallet wallet = walletRepository.getById(walletId);
        wallet.setHoldings(holdings);
        return wallet;
    }

    public User jwtUtility(String token) {

        String key = "securesecuresecuresecureecuresecuresecuresecureecuresecuresecuresecureecuresecuresecuresecure";
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .parseClaimsJws(token.replace("Bearer ", ""));
        Claims body = claimsJws.getBody();
        Optional<User> newUser = userRepository.findUserByUsername(body.getSubject());
        if(newUser.isPresent()) {
            User user = newUser.get();
            return user;
        }
        throw new UsernameNotFoundException("User name not found!");
    }

    public void saveHolding(Order order) {
        Set<Holding> holding = findHolding(order.getWalletId());
        for(Holding hld : holding) {
            if(hld.getTicker().equals(order.getTicker())) {
                Holding complete = holdingRepository.save(combineHoldings(hld, holdingBuilder(order)));
                logger.info(String.format("Order Complete -- %s shares of %s for a total value of %s saved to wallet %s", order.getQuantity(), order.getTicker(), order.getTotalValue(), complete.getWallet()));
                return;
            }
        }
        Holding complete = holdingRepository.save(holdingBuilder(order));
        logger.info(String.format("Order Complete -- %s shares of %s for a total value of %s saved to wallet %s", order.getQuantity(), order.getTicker(), order.getTotalValue(), complete.getWallet()));
    }

    public Holding combineHoldings(Holding old, Holding order) {
        logger.info(String.format("Updating holding of %s in wallet #%s", order.getTicker(), old.getWallet()));
        old.setQuantity(old.getQuantity() + order.getQuantity());
        old.setPrice(order.getPrice());
        old.setTotalValue();
        return old;
    }

    public Holding holdingBuilder(Order order) {
        Holding newHolding = new Holding();
        newHolding.setTicker(order.getTicker());
        newHolding.setPrice(order.getPrice());
        newHolding.setQuantity(order.getQuantity());
        newHolding.setTotalValue();
        newHolding.setWallet(order.getWalletId());
        completePurchase(newHolding);
        return newHolding;
    }

    public Set<Holding> findHolding(int wallet_id) {
        return holdingRepository.findHoldingByWalletId(wallet_id);
    }

    public Wallet walletRetriever(int walletId) {
        return walletRepository.getById(walletId);
    }

    public void updateWallet(Order order) {
        Optional<Double> newTotal = holdingRepository.findTotalValue(order.getWalletId());
        Wallet wallet = walletRepository.getById(order.getWalletId());
        newTotal.ifPresent(wallet::setTotalValue);
        walletRepository.save(wallet);
    }

    public void completePurchase(Holding holding) {
        Wallet wallet = walletRetriever(holding.getWallet());
        wallet.setTotalCash(wallet.getTotalCash() - holding.getTotalValue());
    }

    public boolean checkExistingBalance(Order order) {
        Wallet wallet = walletRetriever(order.getWalletId());
        return wallet.getTotalCash() > order.getTotalValue();
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
        for(HoldingsUpdateDTO holding : holdings) {
            logger.info(String.format("Updating %s with current price %s", holding.getTicker(), holding.getPrice()));
            holdingRepository.updateTickerPrices(holding.getPrice(), holding.getChange(), holding.getTicker());
        }
        List<Holding> allHoldings = holdingRepository.findAll();
        for(Holding hld : allHoldings) {
            hld.setTotalValue();
            logger.info(String.format("Updating total value of %s - %s", hld.getTicker(), hld.getTotalValue()));
            holdingRepository.updateTotalValue(hld.getTotalValue(), hld.getTicker(), hld.getWallet());
        }
        updateAllWallets();
    }

    public void updateAllWallets() {
        List<Wallet> allWallets = walletRepository.findAll();
        for(Wallet wallet : allWallets) {
            Optional<Double> newTotal = holdingRepository.findTotalValue(wallet.getWalletId());
            newTotal.ifPresent(wallet::setTotalValue);
            walletRepository.save(wallet);
        }
    }








}
