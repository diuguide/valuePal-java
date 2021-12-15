package com.example.valuepaljava.controllers;


import com.example.valuepaljava.exceptions.InsufficientFundsException;
import com.example.valuepaljava.models.Order;
import com.example.valuepaljava.models.Wallet;
import com.example.valuepaljava.service.WalletService;

import com.example.valuepaljava.util.JsonUtil;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/stock")
public class WalletController {

    private final Logger logger = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;
    private final JsonUtil jsonUtil;

    @Autowired
    public WalletController(WalletService walletService, JsonUtil jsonUtil) {
        this.walletService = walletService;
        this.jsonUtil = jsonUtil;
    }

    @PostMapping(value="/addStock")
    public ResponseEntity<Object> testStudentRole(@RequestHeader HttpHeaders headers, @RequestBody Order order){
        logger.info(String.format("[BUY] Purchase order for %s at $%s. Total price: %s", order.getTicker(), order.getPrice(), order.getTotalValue()));

        try {
            return ResponseEntity.ok().body(walletService.entryPointBuy(order, headers.getFirst("Authorization")));
        } catch (Exception e) {
            logger.info(String.format("[INSUFFICIENT FUNDS] %s order failed due to insufficient funds", walletService.jwtUtility(Objects.requireNonNull(headers.getFirst("Authorization"))).getUsername()));
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

//    @GetMapping(value="/retrieve")
//    public Wallet retrieveWallet(@RequestHeader HttpHeaders headers){
//
//        return walletService.entryWallet(Objects.requireNonNull(headers.getFirst("Authorization")));
//    }

    @GetMapping(value="/updateHoldings")
    public String updateHoldings() throws ParseException {

        walletService.updateHoldingsTable(jsonUtil.jsonParser(walletService.updateAllHoldings()));

        return "SUCCESS";
    }

//    @PostMapping(value="/saveWallet")
//    public String saveWallet(@RequestHeader HttpHeaders headers, @RequestBody List<HoldingsUpdateDTO> holdings) {
//        walletService.updateHoldingsTable(holdings);
//        return "Success";
//    }

    @PostMapping(value="/sellStock")
    public ResponseEntity<Object> sellHoldingOrder(@RequestHeader HttpHeaders headers, @RequestBody Order order) {
        logger.info(String.format("[SELL] Sell order for %s at $%s. Has been initiated", order.getTicker(), order.getPrice()));
        try {
            Order completedOrder = walletService.entryPointSell(order, headers.getFirst("Authorization"));
            return ResponseEntity.ok().body(completedOrder);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
