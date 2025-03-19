package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.model.Users;
import com.example.attendance.repository.CalculatorRepository;

@Service
public class CalculatorService {

	@Autowired
	private CalculatorRepository calculatorRepository;

	/**
	 * userとmonthでsqlのプロシージャを利用し、総勤務時間と出勤日数を更新
	 *
	 * @param user 更新されたいuser  
	 * @param month 更新されたいmonth
	
	 */
	public void updateCalculator(Users user, Integer month) {
		if (user != null) {
			calculatorRepository.updateCalculator(user.getId(), month);
		} else {
			throw new IllegalArgumentException("User cannot be null");
		}
	}

	/**
	 *すべての総勤務時間と出勤日数を更新
	 */
	public void updateCalculator() {
		calculatorRepository.updateCalculator(null, null);
	}
}
