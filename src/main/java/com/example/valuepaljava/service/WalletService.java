package com.example.valuepaljava.service;

import com.example.valuepaljava.models.Holding;
import com.example.valuepaljava.models.Order;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.models.Wallet;
import com.example.valuepaljava.repos.HoldingRepository;
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

import java.util.Optional;
import java.util.Set;

@Service
public class WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final HoldingRepository holdingRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    @Autowired
    public WalletService(HoldingRepository holdingRepository, WalletRepository walletRepository, UserRepository userRepository) {
        this.holdingRepository = holdingRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    public void entryPoint(Order order, String token) {
        order.setWalletId(jwtUtility(token));
        saveHolding(order);
        updateWallet(order);
        logger.info(String.format("Wallet %s updated", order.getWalletId()));
    }

    public int jwtUtility(String token) {
        String key = "securesecuresecuresecureecuresecuresecuresecureecuresecuresecuresecureecuresecuresecuresecure";
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(key.getBytes()))
                .parseClaimsJws(token.replace("Bearer ", ""));
        Claims body = claimsJws.getBody();
        Optional<User> newUser = userRepository.findUserByUsername(body.getSubject());
        if(newUser.isPresent()) {
            User user = newUser.get();
            return user.getWallet().getWalletId();
        }
        throw new UsernameNotFoundException("User name not found!");
    }

    public void saveHolding(Order order) {
        Set<Holding> holding = findHolding(order.getWalletId());
        for(Holding hld : holding) {
            if(hld.getTicker().equals(order.getTicker())) {
                Holding complete = holdingRepository.save(combineHoldings(hld, holdingBuilder(order)));
                logger.info(String.format("Order Complete -- %s shares of %s for a total value of %s saved to wallet %s", order.getQuantity(), order.getTicker(), order.getTotalValue(), complete.getWallet().getWalletId()));
                return;
            }
        }
        Holding complete = holdingRepository.save(holdingBuilder(order));
        logger.info(String.format("Order Complete -- %s shares of %s for a total value of %s saved to wallet %s", order.getQuantity(), order.getTicker(), order.getTotalValue(), complete.getWallet().getWalletId()));
    }

    public Holding combineHoldings(Holding old, Holding order) {
        logger.info(String.format("Updating holding of %s in wallet #%s", order.getTicker(), old.getWallet().getWalletId()));
        old.setQuantity(old.getQuantity() + order.getQuantity());
        old.setPrice(order.getPrice());
        old.setTotalValue();
        return old;
    }

    public Holding holdingBuilder(Order order) {
        Wallet currentWallet = walletRetriever(order.getWalletId());
        Holding newHolding = new Holding();
        newHolding.setTicker(order.getTicker());
        newHolding.setPrice(order.getPrice());
        newHolding.setQuantity(order.getQuantity());
        newHolding.setTotalValue();
        newHolding.setWallet(currentWallet);
        completePurchase(newHolding);
        return newHolding;
    }

    public Set<Holding> findHolding(int wallet_id) {
        return holdingRepository.findHoldingByWallet_WalletId(wallet_id);
    }

    public Wallet walletRetriever(int walletId) {
        return walletRepository.getById(walletId);
    }

    public void updateWallet(Order order) {
        Optional<Double> newTotal = holdingRepository.findTotalValue(order.getWalletId());
        Wallet wallet = walletRepository.getById(order.getWalletId());
        wallet.setTotalValue(newTotal.get());
        walletRepository.save(wallet);
    }

    public void completePurchase(Holding holding) {
        Wallet wallet = holding.getWallet();
        wallet.setTotalCash(wallet.getTotalCash() - holding.getTotalValue());
    }








}
