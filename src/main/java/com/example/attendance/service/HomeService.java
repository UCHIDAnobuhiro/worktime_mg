package com.example.attendance.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.attendance.dto.WorkTimeResponse;
import com.example.attendance.model.Calculator;
import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;
import com.example.attendance.repository.CalculatorRepository;
import com.example.attendance.repository.StampRepository;

@Service
public class HomeService {
	private final StampRepository stampRepository;
	private final UsersService usersService;
	private final CalculatorRepository calculatorRepository;

	public HomeService(
			CalculatorRepository calculatorRepository,
			StampRepository stampRepository,
			UsersService usersService) {
		this.calculatorRepository = calculatorRepository;
		this.stampRepository = stampRepository;
		this.usersService = usersService;
	}

	// calculatorとSTAMPのすべてのデータを取得
	public void prepareHomeData(Model model) {
		// ログイン中のユーザを取得
		Users loggedInUser = usersService.getLoggedInUserOrThrow();
		model.addAttribute("user", loggedInUser);

		// ユーザに紐づくCalculatorの取得
		List<Calculator> calculators = calculatorRepository.findByUser(loggedInUser);
		model.addAttribute("calculators", calculators);

		// 現在の月を初期設定にする
		Integer selectedMonth = LocalDate.now().getMonthValue();
		model.addAttribute("selectedMonth", selectedMonth);
	}

	//月に対する総勤務時間と出勤日数の更新
	public WorkTimeResponse getWorkTimeByMonth(Integer month) {
		// ログイン中のユーザを取得
		Users loggedInUser = usersService.getLoggedInUserOrThrow();

		// データベースに month + user でデータを取得し、ない場合はデフォルト値を返す
		Optional<Calculator> calculatorOpt = calculatorRepository.findByUserAndMonth(loggedInUser, month);
		if (calculatorOpt.isEmpty()) {
			return new WorkTimeResponse("00:00:00", 0);
		}
		Calculator calculator = calculatorOpt.get();
		return new WorkTimeResponse(
				calculator.getWorkTimeMonth(),
				calculator.getWorkDaysMonth());

	}

	// 指定月のスタンプデータを取得し更新
	public List<Stamp> getStampsByMonth(Integer month) {
		// ログイン中のユーザを取得
		Users loggedInUser = usersService.getLoggedInUserOrThrow();

		// ログインユーザーに紐づく全てのスタンプ情報を取得
		List<Stamp> allStamps = stampRepository.findByUser(loggedInUser);

		// 指定された月に該当するスタンプのみをフィルタリングしてreturn
		return allStamps.stream()
				.filter(stamp -> {
					LocalDate localDate = stamp.getDay().toLocalDate();
					return localDate.getMonthValue() == month;
				})
				.collect(Collectors.toList());
	}
}