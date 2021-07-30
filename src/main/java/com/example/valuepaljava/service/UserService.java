package com.example.valuepaljava.service;

import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private Logger logger = LoggerFactory.getLogger(UserService.class);
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User validateUser(User user) {
        isUsernameAvailable(user.getUsername());
        isEmailAvailable(user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User newUser = userRepository.save(user);
        logger.info(String.format("User: %s with userId: %s added to the database", newUser.getUsername(), newUser.getUserId()));
        return user;
    }

    private boolean isUsernameAvailable(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            throw new InvalidInputException(String.format("Username: %s not available!", username));
        }
        return true;
    }

    private boolean isEmailAvailable(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            throw new InvalidInputException(String.format("Email: %s not available!", email));
        }
        return true;
    }
}
