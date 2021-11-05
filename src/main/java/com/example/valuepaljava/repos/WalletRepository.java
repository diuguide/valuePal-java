package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Holding, Integer> {
}
