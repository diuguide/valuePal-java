package com.example.valuepaljava.service;

import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.jwt.Credentials;
import com.example.valuepaljava.jwt.JwtTokenGenerator;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.repos.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenGenerator jwtTokenGenerator) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenGenerator = jwtTokenGenerator;
    }

    public String authenticate(Credentials creds) {
        Optional<User> user = userRepository.findUserByUsername(creds.getUsername());
        if(user.isPresent()) {
            if(verify(creds.getPassword(), user.get().getPassword())) {
                return jwtTokenGenerator.generateToken(user.get().getUsername());
            } else {
                throw new InvalidInputException("Password Incorrect");
            }
        } else {
            throw new InvalidInputException(String.format("User %s does not exist!", creds.getUsername()));
        }
    }

    private boolean verify(String password, String hash) {
        return passwordEncoder.matches(password, hash);
    }

}
