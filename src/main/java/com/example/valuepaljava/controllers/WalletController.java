package com.example.valuepaljava.controllers;

import com.example.valuepaljava.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class WalletController {

    private final Logger logger = LoggerFactory.getLogger(WalletController.class);
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping(value="/addStock")
    public String testStudentRole(){
        return "Test Student Route Working!";
    }

    @GetMapping(value="/extend/ball")
    public String extendBall(){
        return "Extend Ball Route Working!";
    }

}
