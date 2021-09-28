package com.example.valuepaljava.repos;

import com.example.valuepaljava.models.SummaryObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SummaryRepository extends JpaRepository<SummaryObject, Integer> {
}
