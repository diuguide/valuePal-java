package com.example.valuepaljava.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ApplicationUserDao extends JpaRepository<ApplicationUser, Integer> {

    Optional<ApplicationUser> findApplicationUserByUsername(String username);

}
