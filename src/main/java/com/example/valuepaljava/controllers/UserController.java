package com.example.valuepaljava.controllers;

import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.models.Wallet;
import com.example.valuepaljava.service.UserService;
import com.example.valuepaljava.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final WalletService walletService;

    @Autowired
    public UserController(UserService userService, WalletService walletService) {
        this.userService = userService;
        this.walletService = walletService;
    }

    @PostMapping(value="/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if(user != null) {
            try {
                return ResponseEntity.ok().body(userService.saveUser(user));
            } catch (InvalidInputException e) {
                logger.info(e.getMessage());
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.badRequest().body("Request is empty");
    }

    @GetMapping(value="/getUserInfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader HttpHeaders headers) {
        if(headers.getFirst("Authorization") != null) {
            try {
                return ResponseEntity
                        .ok()
                        .body(userService.getUserInfo(Objects.requireNonNull(headers.getFirst("Authorization"))));
            } catch (InvalidInputException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.status(403).body("403: Forbidden");
    }

    @GetMapping(value="/getUserOrders")
    public ResponseEntity<?> getUserOrders(@RequestHeader HttpHeaders headers) {
    if(headers.getFirst("Authorization") != null) {
        try {
            return ResponseEntity.ok().body(walletService.getUserOrders(Objects.requireNonNull(headers.getFirst("Authorization"))));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
      return ResponseEntity.status(403).body("403: Forbidden");
    }

    @GetMapping(value="/getUserHoldings")
    public ResponseEntity<?> getUserHoldings(@RequestHeader HttpHeaders headers) {
        if(headers.getFirst("Authorization") != null) {
            try {
                return ResponseEntity.ok().body(walletService.getUserHoldings(Objects.requireNonNull(headers.getFirst("Authorization"))));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.status(403).body("403: Forbidden");

    }

    @GetMapping(value="/getUserCash")
    public ResponseEntity<?> getUserCash(@RequestHeader HttpHeaders headers) {
        if(headers.getFirst("Authorization") != null) {
            try {
                return ResponseEntity.ok().body(userService.getTotalCash(Objects.requireNonNull(headers.getFirst("Authorization"))));
            } catch (Exception e) {
                logger.info(e.toString());
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
        return ResponseEntity.status(403).body("403: Forbidden");
    }
}
