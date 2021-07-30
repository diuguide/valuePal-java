package com.example.valuepaljava.controllers;

import com.example.valuepaljava.models.User;
import com.example.valuepaljava.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path="/all", produces= APPLICATION_JSON_VALUE)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping(path="/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public User registerUser(@RequestBody User user) {
        return userRepository.save(user);
    }


}
