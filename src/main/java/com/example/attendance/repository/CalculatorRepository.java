package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Calculator;

@Repository
public interface CalculatorRepository extends JpaRepository<Calculator, Long> {

	Calculator findByUserIdAndMonth(Long userId, Integer month);

	@Query("SELECT DISTINCT c.month FROM Calculator c WHERE c.user_id = :userId ORDER BY c.month")
	List<Integer> findDistinctMonthsByUserId(@Param("userId") Long userId);

	List<Calculator> findByUserId(@Param("userId") Long userId);

}
