package com.example.valuepaljava.service;

import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.Holding;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.models.Wallet;
import com.example.valuepaljava.repos.HoldingRepository;
import com.example.valuepaljava.repos.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletService walletService;
    private final HoldingRepository holdingRepository;
    private final Pattern nameValidation = Pattern.compile("^[a-zA-z]+$");
    private final Pattern userNamePattern = Pattern.compile("[A-Za-z0-9_]+");
    private final Pattern passwordPattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    private final Pattern emailPattern = Pattern.compile("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, WalletService walletService, HoldingRepository holdingRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
        this.holdingRepository = holdingRepository;
    }

    public User getUserInfo(String token) {
        User userInfo = walletService.jwtUtility(token);
        Set<Holding> holdings = holdingRepository.findHoldingByWalletIdOrderByQuantityDesc(userInfo.getWallet().getWalletId());
        if(holdings.size() > 0) {
            userInfo.getWallet().setHoldings(holdings);
        }
        userInfo.setPassword(null);
        return userInfo;
    }

    public boolean validateUser(User user) {
        isNameValid(user.getFirstName());
        isUsernameValid(user.getUsername());
        isPasswordValid(user.getPassword());
        isEmailValid(user.getEmail());
        isUsernameAvailable(user.getUsername());
        isEmailAvailable(user.getEmail());
        return true;
    }

    public User saveUser(User user) {
        if(validateUser(user)) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setWallet(new Wallet(25000));
            User newUser = userRepository.save(user);
            logger.info(String.format("[REGISTER] User: %s with userId: %s has been registered", newUser.getUsername(), newUser.getUserId()));
            return user;
        };
        throw new InvalidInputException("An error occurred");
    }

    public boolean isUsernameAvailable(String username) {
        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            throw new InvalidInputException(String.format("Username: %s not available!", username));
        }
        return true;
    }

    public boolean isEmailAvailable(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        if (user.isPresent()) {
            throw new InvalidInputException(String.format("Email: %s not available!", email));
        }
        return true;
    }

    public boolean isNameValid(String firstName) {
        if(nameValidation.matcher(firstName).matches()) {
            return true;
        }
        throw new InvalidInputException(String.format("First name can only consist of characters a-z. %s is invalid!", firstName));
    }

    public boolean isUsernameValid(String username) {
        if(userNamePattern.matcher(username).matches()) {
            return true;
        }
        throw new InvalidInputException(String.format(" %s is not a valid username", username));
    }

    public boolean isPasswordValid(String password) {
        if(passwordPattern.matcher(password).matches()) {
            return true;
        }
        throw new InvalidInputException("Passwords must include a minimum of eight characters, at least one uppercase letter, one lowercase letter, one number and one special character");
    }

    public boolean isEmailValid(String email) {
        if(emailPattern.matcher(email).matches()) {
            return true;
        }
        throw new InvalidInputException("Not a valid email address!");
    }

}
