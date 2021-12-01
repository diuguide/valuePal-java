package com.example.valuepaljava.controllers;

import com.example.valuepaljava.auth.ApplicationUser;
import com.example.valuepaljava.auth.ApplicationUserService;
import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.jwt.Credentials;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.service.AuthenticationService;
import com.example.valuepaljava.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value="/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        logger.info("Adding user to database...");
        try {
            return ResponseEntity.ok().body(userService.saveUser(user));
        } catch (InvalidInputException e) {
            logger.info(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
