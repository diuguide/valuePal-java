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

    public Order entryPointSell(Order order, String token) throws InsufficientFundsException{
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        User currentUser = jwtUtility(token);
        order.setWalletId(currentUser.getWallet().getWalletId());
        order.setStatus("Pending");
        orderRepository.save(order);
        Optional<Holding> existingHolding = checkForHolding(order);
        if(existingHolding.isPresent()) {
            if(existingHolding.get().getQuantity() >= order.getQuantity()) {
                existingHolding.get().setQuantity(existingHolding.get().getQuantity() - order.getQuantity());
                if(existingHolding.get().getQuantity() == 0) {
                    holdingRepository.delete(existingHolding.get());
                    logger.info(String.format("[SELL] Entire holding of %s sold and holding entry removed.", order.getTicker()));
                } else {
                    existingHolding.get().setTotalValue();
                    Holding newHolding = holdingRepository.save(existingHolding.get());
                    logger.info(String.format("[SELL] Holding Updated: Wallet ID: %s updated with %s shares of %s", newHolding.getWallet(), newHolding.getQuantity(), newHolding.getTicker()));
                }
                currentUser.getWallet().setTotalCash(currentUser.getWallet().getTotalCash() + order.getTotalValue());
//                Wallet newWallet = walletRepository.save(currentUser.getWallet());
//                updateWallet(currentUser.getWallet().getWalletId());
                //System.out.println("function sql" + holdingRepository.updateAvgPrice());
//                logger.info(String.format("[SELL] Wallet ID: %s updated after sale of %s", newWallet.getWalletId(), order.getTicker()));
                order.setStatus("Filled");
                order.setOrderType('S');
                Order filledOrder = orderRepository.save(order);
                long endTime = System.currentTimeMillis();
                duration = endTime - startTime;
                logger.info(String.format("[SELL] Order #%s has been filled.  Duration : %s/ms", filledOrder.getId(), duration));
                return filledOrder;
            } else {
                order.setStatus("Rejected");
                orderRepository.save(order);
                long endTime = System.currentTimeMillis();
                duration = endTime - startTime;
                logger.info(String.format("Order #%s has been rejected. Not enough stock in wallet to fill order. Duration: %s/ms", order.getId(), duration));
                throw new InsufficientFundsException(String.format("You do not own enough %s stock to complete this order!", order.getTicker()));
            }
        } else {
            order.setStatus("Rejected");
            orderRepository.save(order);
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("Order #%s has been rejected. Not enough stock in wallet to fill order. Duration: %s/ms", order.getId(), duration));
            throw new InsufficientFundsException(String.format("You do not own any %s", order.getTicker()));
        }
    }

    public Order entryPointBuy(Order order, String token) {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        User currentUser = jwtUtility(token);
        order.setWalletId(currentUser.getWallet().getWalletId());
        order.setStatus("Pending");
        orderRepository.save(order);
        if(checkExistingBalance(order)) {
            Optional<Holding> existingHolding = checkForHolding(order);
            if(existingHolding.isPresent()) {
                existingHolding.get().setQuantity(order.getQuantity()+existingHolding.get().getQuantity());
                existingHolding.get().setTotalValue();
                holdingRepository.save(existingHolding.get());
            } else {
                System.out.println("order.price " + order.getPrice());
                Holding newHolding = new Holding(
                        order.getWalletId(),
                        order.getTicker(),
                        order.getQuantity(),
                        order.getPrice(),
                        order.getTotalValue(),
                        order.getPrice()
                );
                System.out.println("New Holding: " + newHolding);
                Holding savedHolding = holdingRepository.save(newHolding);
                logger.info(String.format("[BUY] New Holding created in wallet %s, holding id %s", savedHolding.getWallet(), savedHolding.getId()));
            }
            currentUser.getWallet().setTotalCash(currentUser.getWallet().getTotalCash() - order.getTotalValue());
            Wallet newWallet = walletRepository.save(currentUser.getWallet());
//            updateWallet(currentUser.getWallet().getWalletId());
            logger.info(String.format("[BUY] Wallet ID: %s updated after sale of %s", newWallet.getWalletId(), order.getTicker()));
            order.setStatus("Filled");
            order.setOrderType('B');
            Order filledOrder = orderRepository.save(order);
            //System.out.println("function sql" + holdingRepository.updateAvgPrice());
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("[BUY] Order #%s has been filled, Duration: %s/ms", filledOrder.getId(), duration));
            return filledOrder;
        } else {
            order.setStatus("Rejected");
            orderRepository.save(order);
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("[BUY] Order failed! Insufficient funds, duration: %s", duration));
            throw new InsufficientFundsException(String.format("Insufficient funds! $%s is needed to complete this order.", order.getTotalValue()));
        }
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

    public boolean checkExistingBalance(Order order) {
        Wallet wallet = walletRetriever(order.getWalletId());
        return wallet.getTotalCash() > order.getTotalValue();
    }

    public Wallet walletRetriever(int walletId) {
        return walletRepository.getById(walletId);
    }

    public Optional<Holding> checkForHolding(Order order) {
        return holdingRepository.findHoldingByWalletAndAndTicker(order.getTicker(), order.getWalletId());
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
    public void updateHoldingsTable(Set<Quote> quotes) {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        for(Quote q : quotes) {
            holdingRepository.updateTickerPrices(q.getPrice(), q.getChange(), q.getSymbol());
        }
        long endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        logger.info(String.format("[UPDATE] Updated holding information.  Updated in %s ms", duration));
    }

}
