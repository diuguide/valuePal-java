package com.example.valuepaljava.controllers;

import com.example.valuepaljava.jwt.JwtTokenVerifier;
import com.example.valuepaljava.models.Order;
import com.example.valuepaljava.service.WalletService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

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
    public String testStudentRole(@RequestHeader HttpHeaders headers, @RequestBody Order order){
        logger.info(String.format("Purchase order for %s at $%s. Total price: %s", order.getTicker(), order.getPrice(), order.getTotalValue()));
        walletService.entryPoint(order, headers.getFirst("Authorization"));
        return "success";
    }

    @GetMapping(value="/extend/ball")
    public String extendBall(){
        return "Extend Ball Route Working!";
    }

}
