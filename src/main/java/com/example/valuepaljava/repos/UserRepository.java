package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
