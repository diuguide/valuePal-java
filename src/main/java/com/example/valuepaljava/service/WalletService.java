package com.example.valuepaljava.service;

import com.example.valuepaljava.exceptions.InsufficientFundsException;
import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.*;
import com.example.valuepaljava.repos.*;
import com.example.valuepaljava.util.JsonUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WalletService {

    private final Logger logger = LoggerFactory.getLogger(WalletService.class);
    private final HoldingRepository holdingRepository;
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final HoldingRecordRepository holdingRecordRepository;
    private final StockService stockService;
    private final JsonUtil jsonUtil;

    @Autowired
    public WalletService(HoldingRepository holdingRepository, WalletRepository walletRepository, UserRepository userRepository, OrderRepository orderRepository, HoldingRecordRepository holdingRecordRepository, StockService stockService, JsonUtil jsonUtil) {
        this.holdingRepository = holdingRepository;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.holdingRecordRepository = holdingRecordRepository;
        this.stockService = stockService;
        this.jsonUtil = jsonUtil;
    }

    public Order entryPointSell(Order order, String token) throws InsufficientFundsException{
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        User currentUser = jwtUtility(token);
        order.setWalletId(currentUser.getWallet().getWalletId());
        Optional<Holding> existingHolding = checkForHolding(order);
        if(existingHolding.isPresent()) {
            if(existingHolding.get().getQuantity() >= order.getQuantity()) {
                existingHolding.get().setQuantity(existingHolding.get().getQuantity() - order.getQuantity());
                if(existingHolding.get().getQuantity() == 0) {
                    holdingRepository.delete(existingHolding.get());
                    logger.info(String.format("[SELL] Entire holding of %s sold and holding entry removed.", order.getTicker()));
                } else {
                    existingHolding.get().setTimestamp(new Date());
                    Holding newHolding = holdingRepository.save(existingHolding.get());
                    logger.info(String.format("[SELL] Holding Updated: Wallet ID: %s updated with %s shares of %s", newHolding.getWalletId(), newHolding.getQuantity(), newHolding.getTicker()));
                }
                currentUser.getWallet().setTotalCash(currentUser.getWallet().getTotalCash() + order.getTotalValue());
                order.setStatus("Filled");
                order.setOrderType('S');
                order.setTimestamp(new Date());
                Order filledOrder = orderRepository.save(order);
                long endTime = System.currentTimeMillis();
                duration = endTime - startTime;
                logger.info(String.format("[SELL] Order #%s has been filled.  Duration : %s/ms", filledOrder.getId(), duration));
                return filledOrder;
            } else {
                order.setStatus("Rejected");
                order.setOrderType('S');
                order.setTimestamp(new Date());
                orderRepository.save(order);
                long endTime = System.currentTimeMillis();
                duration = endTime - startTime;
                logger.info(String.format("Order #%s has been rejected. Not enough stock in wallet to fill order. Duration: %s/ms", order.getId(), duration));
                throw new InsufficientFundsException(String.format("You do not own enough %s stock to complete this order!", order.getTicker()));
            }
        } else {
            order.setStatus("Rejected");
            order.setTimestamp(new Date());
            order.setOrderType('S');
            orderRepository.save(order);
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("Order #%s has been rejected. Not enough stock in wallet to fill order. Duration: %s/ms", order.getId(), duration));
            throw new InsufficientFundsException(String.format("You do not own any %s", order.getTicker()));
        }
    }

    public Order entryPointBuy(Order order, String token) throws ParseException {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        User currentUser = jwtUtility(token);
        order.setWalletId(currentUser.getWallet().getWalletId());
        if(checkExistingBalance(order)) {
            Optional<Holding> existingHolding = checkForHolding(order);
            if(existingHolding.isPresent()) {
                existingHolding.get().setQuantity(order.getQuantity()+existingHolding.get().getQuantity());
                existingHolding.get().setTimestamp(new Date());
            } else {
                Holding newHolding = new Holding(
                        order.getWalletId(),
                        order.getTicker(),
                        order.getQuantity()
                );
                newHolding.setTimestamp(new Date());
                Holding savedHolding = holdingRepository.save(newHolding);
                logger.info(String.format("[BUY] New Holding created in wallet %s, holding id %s", savedHolding.getWalletId(), savedHolding.getId()));
            }
            currentUser.getWallet().setTotalCash(currentUser.getWallet().getTotalCash() - order.getTotalValue());
            Wallet newWallet = walletRepository.save(currentUser.getWallet());
            logger.info(String.format("[BUY] Wallet ID: %s updated after purchase of %s", newWallet.getWalletId(), order.getTicker()));
            order.setStatus("Filled");
            order.setOrderType('B');
            order.setTimestamp(new Date());
            Order filledOrder = orderRepository.save(order);
            existingHolding.ifPresent(holdingRepository::save);
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("[BUY] Order #%s has been filled, Duration: %s/ms", filledOrder.getId(), duration));
            return filledOrder;
        } else {
            order.setStatus("Rejected");
            order.setOrderType('B');
            orderRepository.save(order);
            long endTime = System.currentTimeMillis();
            duration = endTime - startTime;
            logger.info(String.format("[BUY] Order failed! Insufficient funds, duration: %s", duration));
            throw new InsufficientFundsException(String.format("Insufficient funds! $%s is needed to complete this order.", order.getTotalValue()));
        }
    }

    public User jwtUtility(String token) {
        String key = "securesecuresecuresecureecuresecuresecuresecureecuresecuresecuresecureecuresecuresecuresecure";
        if(token != null) {
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
        throw new InvalidInputException("403: Forbidden");


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

    public double getAvgPurchasePrice(String token, String ticker) {

        User newUser = jwtUtility(token);
        Double avgPurchasePrice = orderRepository.getAvgPurchasePrice(ticker, newUser.getWallet().getWalletId());

        return avgPurchasePrice;
    }

    public Set<Order> getUserOrders(String token) {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        User currentUser = jwtUtility(token);
        long endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        Set<Order> orders = orderRepository.getOrdersByWalletId(currentUser.getWallet().getWalletId());
        if(orders.size() > 0) {
            logger.info(String.format("[DATA] %s retrieved all orders. Duration %s ms", currentUser.getUsername(), duration));
            return orders;
        }
        throw new InvalidInputException("No orders found!");
    }

    public Set<HoldingRecord> getUserHoldings(String token) {
        long startTime = System.currentTimeMillis();
        long duration = 0L;
        User currentUser = jwtUtility(token);


        long endTime = System.currentTimeMillis();
        duration = endTime - startTime;

        Set<HoldingRecord> withAvg = holdingRecordRepository.createHoldingRow("PROG", 1);

        // Set<Holding> holdings = holdingRepository.findHoldingByWalletIdOrderByQuantityDesc(currentUser.getWallet().getWalletId());
        if(withAvg.size() > 0) {
            logger.info(String.format("[DATA] %s retrieved all holdings. Duration %s ms", currentUser.getUsername(), duration));
            return withAvg;
        }

        throw new InvalidInputException("No holdings found!");
    }

}
