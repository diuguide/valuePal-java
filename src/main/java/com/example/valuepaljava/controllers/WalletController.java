package com.example.valuepaljava.controllers;


import com.example.valuepaljava.models.HoldingsUpdateDTO;
import com.example.valuepaljava.models.Order;
import com.example.valuepaljava.models.Wallet;
import com.example.valuepaljava.service.WalletService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping("/stock")
public class WalletController {

    private final Logger logger = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value="/addStock")
    public ResponseEntity<Object> testStudentRole(@RequestHeader HttpHeaders headers, @RequestBody Order order){
        logger.info(String.format("Purchase order for %s at $%s. Total price: %s", order.getTicker(), order.getPrice(), order.getTotalValue()));
        try {
            walletService.entryPoint(order, headers.getFirst("Authorization"));
        } catch (Exception e) {
            logger.info(String.format("[INSUFFICIENT FUNDS] %s order failed due to insufficient funds", walletService.jwtUtility(Objects.requireNonNull(headers.getFirst("Authorization"))).getUsername()));
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(order);
    }

    @GetMapping(value="/retrieve")
    public Wallet retrieveWallet(@RequestHeader HttpHeaders headers){

        return walletService.entryWallet(Objects.requireNonNull(headers.getFirst("Authorization")));
    }

    @GetMapping(value="/updateHoldings")
    public String updateHoldings(@RequestHeader HttpHeaders headers) throws JsonProcessingException {
        return walletService.updateAllHoldings();
    }

    @PostMapping(value="/saveWallet")
    public String saveWallet(@RequestHeader HttpHeaders headers, @RequestBody List<HoldingsUpdateDTO> holdings) {
        walletService.updateHoldingsTable(holdings);
        return "Success";
    }


}
