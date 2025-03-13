package com.example.attendance.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.example.attendance.repository.CalculatorRepository;
import com.example.attendance.repository.StampRepository;

@Controller
public class HomeController {

	@Autowired
	private StampRepository stampRepository;

	@Autowired
	private CalculatorRepository calculatorRepository;

	//calculatorとSTAMPのすべてのデータを取得
	@GetMapping("/worktime/home")
	public String showHome(Model model) {

		//user_idは1に限定する、ログイン機能と連結すると修正
		Long userId = 2L;

		List<Calculator> calculators = calculatorRepository.findByUserId(userId);
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
		//暫定user_id＝1
		Long userId = 1L;

		// データベースにmonth+useridでデータを取得し、ない場合404
		Calculator calculator = calculatorRepository.findByUserIdAndMonth(userId, month);
		if (calculator == null) {
			return ResponseEntity.notFound().build();
		}

		// 月に対するcalculatorをresponseに追加
		Map<String, Object> response = new HashMap<>();
		response.put("workTimeMonth", calculator.getWorkTimeMonth());
		response.put("workDaysMonth", calculator.getWorkDaysMonth());
		return ResponseEntity.ok(response);
	}

	//月に対するstampの更新
	@GetMapping("/updateStampsByMonth/{month}")
	@ResponseBody
	public ResponseEntity<List<Stamp>> getStampsByMonth(@PathVariable Integer month, HttpSession session) {

		//暫定userId
		Long userId = 1L;
		List<Stamp> allStamps = stampRepository.findByUserId(userId); // USER_IDに対するすべてのstampを取得

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
