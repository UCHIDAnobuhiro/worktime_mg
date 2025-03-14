package com.example.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.attendance.model.Calculator;
import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;
import com.example.attendance.repository.CalculatorRepository;
import com.example.attendance.repository.StampRepository;
import com.example.attendance.service.UsersService;

@Controller
public class HomeController {

	@Autowired
	private StampRepository stampRepository;

	@Autowired
	private CalculatorRepository calculatorRepository;

	@Autowired
	private UsersService usersService;

	//calculatorとSTAMPのすべてのデータを取得
	@GetMapping("/worktime/home")
	public String showHome(Model model) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ユーザー情報を Model に追加
		model.addAttribute("user", loggedInUser);

		// Calculator の取得
		List<Calculator> calculators = calculatorRepository.findByUser(loggedInUser);
		model.addAttribute("calculators", calculators);

		//月を選択selecteの初期値を現在の月にする
		Integer selectedMonth = LocalDate.now().getMonthValue();
		model.addAttribute("selectedMonth", selectedMonth);

		return "home";
	}

	//月に対する総勤務時間と出勤日数の更新
	@PatchMapping("/updateWorkTimeByMonth/{month}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateWorkTime(@PathVariable Integer month, Model model,
			HttpSession session) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ユーザー情報を Model に追加
		model.addAttribute("user", loggedInUser);

		// データベースにmonth+useridでデータを取得し、ない場合404
		// Calculator calculator = calculatorRepository.findByUserIdAndMonth(userId, month);

		// データベースに month + user でデータを取得し、ない場合はデフォルト値を返す
		Optional<Calculator> calculatorOpt = calculatorRepository.findByUserAndMonth(loggedInUser, month);
		Map<String, Object> response = new HashMap<>();
		if (calculatorOpt.isEmpty()) {
			response.put("workTimeMonth", "00:00:00");
			response.put("workDaysMonth", "0");
		} else {
			Calculator calculator = calculatorOpt.get();
			response.put("workTimeMonth", calculator.getWorkTimeMonth());
			response.put("workDaysMonth", calculator.getWorkDaysMonth());
		}

		// 月に対するcalculatorをresponseに追加
		return ResponseEntity.ok(response);
	}

	//月に対するstampの更新
	@GetMapping("/updateStampsByMonth/{month}")
	@ResponseBody
	public ResponseEntity<List<Stamp>> getStampsByMonth(@PathVariable Integer month, HttpSession session) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		List<Stamp> allStamps = stampRepository.findByUser(loggedInUser); // USER_IDに対するすべてのstampを取得

		//stampのday列をmonth化し、選択された月のみ分をlist化する
		List<Stamp> filteredStamps = allStamps.stream()
				.filter(stamp -> {
					LocalDate localDate = stamp.getDay().toLocalDate(); //localdateで時間計算を行う
					return localDate.getMonthValue() == month;
				})
				.collect(Collectors.toList());
		return ResponseEntity.ok(filteredStamps);
	}

}
