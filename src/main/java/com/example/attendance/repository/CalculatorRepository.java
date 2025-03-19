package com.example.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Calculator;
import com.example.attendance.model.Users;

@Repository
public interface CalculatorRepository extends JpaRepository<Calculator, Long> {

	Calculator findByUserIdAndMonth(Long userId, Integer month);

	Optional<Calculator> findByUserAndMonth(Users user, Integer month);

	List<Calculator> findByUser(Users user);

	@Procedure(procedureName = "update_calculator")
	void updateCalculator(@Param("p_user_id") Long userId, @Param("p_month") Integer month);
}
