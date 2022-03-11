package com.example.valuepaljava.controllers;

import com.example.valuepaljava.exceptions.InsufficientFundsException;
import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.Order;
import com.example.valuepaljava.service.StockService;
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
    private final StockService stockService;
    private final JsonUtil jsonUtil;

    @Autowired
    public WalletController(WalletService walletService, StockService stockService, JsonUtil jsonUtil) {
        this.walletService = walletService;
        this.stockService = stockService;
        this.jsonUtil = jsonUtil;
    }

    @PostMapping(value="/addStock")
    public ResponseEntity<Object> testStudentRole(@RequestHeader HttpHeaders headers, @RequestBody Order order){
        logger.info(String.format("[BUY] Purchase received: %s X %s", order.getQuantity(),order.getTicker() ));

        try {
            order.setPrice(jsonUtil.jsonParser(stockService.getTickerData(2, order.getTicker())).iterator().next().getPrice());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cannot find stock data");
        }

        if(headers.getFirst("Authorization") != null) {
            try {
                return ResponseEntity.ok().body(walletService.entryPointBuy(order, headers.getFirst("Authorization")));
            } catch (Exception e) {
                logger.info(String.format("[INSUFFICIENT FUNDS] %s order failed due to insufficient funds", walletService.jwtUtility(Objects.requireNonNull(headers.getFirst("Authorization"))).getUsername()));
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("403: Forbidden");
    }

    @GetMapping(value="/updateHoldings")
    public String updateHoldings() throws ParseException {
        try {
            walletService.updateHoldingsTable(jsonUtil.jsonParser(walletService.updateAllHoldings()));
            return "SUCCESS";
        } catch (Exception e) {
            return "Something went wrong...";
        }

    }

    @PostMapping(value="/sellStock")
    public ResponseEntity<Object> sellHoldingOrder(@RequestHeader HttpHeaders headers, @RequestBody Order order) {
        logger.info(String.format("[SELL] Sell order for %s at $%s. Has been initiated", order.getTicker(), order.getPrice()));

        try {
            order.setPrice(jsonUtil.jsonParser(stockService.getTickerData(2, order.getTicker())).iterator().next().getPrice());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Cannot find stock data");
        }

        try {
            Order completedOrder = walletService.entryPointSell(order, headers.getFirst("Authorization"));
            return ResponseEntity.ok().body(completedOrder);
        } catch (InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
