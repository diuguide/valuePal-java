package com.example.valuepaljava.controllers;

import com.example.valuepaljava.models.User;
import com.example.valuepaljava.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(value="/all", produces= APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(value="/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public User registerUser(@RequestBody User user) {
        System.out.println("Fired");
        logger.info(String.format("User %s has been added to the database!", user));
        return userRepository.save(user);
    }


}
