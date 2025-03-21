package com.example.attendance.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.attendance.model.Calculator;
import com.example.attendance.model.Users;
import com.example.attendance.repository.CalculatorRepository;

@Service
public class HomeService {
	private final UsersService usersService;
	private final CalculatorRepository calculatorRepository;

	public HomeService(UsersService usersService, CalculatorRepository calculatorRepository) {
		this.usersService = usersService;
		this.calculatorRepository = calculatorRepository;
	}

	// calculatorとSTAMPのすべてのデータを取得
	public void prepareHomeData(Model model) {
		// ログイン中のユーザを取得
		Users loggedInUser = usersService.getLoggedInUser();
		if (loggedInUser == null) {
			throw new IllegalStateException("ログインユーザーが取得できませんでした。");
		}
		model.addAttribute("user", loggedInUser);

		// ユーザに紐づくCalculatorの取得
		List<Calculator> calculators = calculatorRepository.findByUser(loggedInUser);
		if (calculators == null) {
			calculators = Collections.emptyList();
		}
		model.addAttribute("calculators", calculators);

		// 現在の月を初期設定にする
		Integer selectedMonth = LocalDate.now().getMonthValue();
		model.addAttribute("selectedMonth", selectedMonth);
	}
}