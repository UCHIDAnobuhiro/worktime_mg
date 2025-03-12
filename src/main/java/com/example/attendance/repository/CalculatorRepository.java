package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Calculator;

@Repository
public interface CalculatorRepository extends JpaRepository<Calculator, Long> {
}
