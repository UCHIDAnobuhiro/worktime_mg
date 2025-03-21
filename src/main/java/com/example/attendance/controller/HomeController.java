package com.example.attendance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.attendance.model.Stamp;
import com.example.attendance.service.HomeService;

@Controller
public class HomeController {
	private final HomeService homeService;

	public HomeController(
			HomeService homeService) {
		this.homeService = homeService;

	}

	@GetMapping("/worktime/home")
	public String showHome(Model model) {
		try {
			homeService.prepareHomeData(model);
			return "home";

		} catch (Exception e) {
			model.addAttribute("errorMessage", "データ取得中にエラーが発生しました" + e.getMessage());
			return "error";
		}
	}

	@PatchMapping("/updateWorkTimeByMonth/{month}")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> updateWorkTime(@PathVariable Integer month) {
		try {
			Map<String, Object> response = homeService.getWorkTimeByMonth(month);
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("error", "勤務時間の取得中にエラーが発生しました: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}
	}

	@GetMapping("/updateStampsByMonth/{month}")
	@ResponseBody
	public ResponseEntity<?> getStampsByMonth(@PathVariable Integer month) {
		try {
			List<Stamp> filteredStamps = homeService.getStampsByMonth(month);
			return ResponseEntity.ok(filteredStamps);
		} catch (Exception e) {
			Map<String, String> error = new HashMap<>();
			error.put("error", "打刻データの取得中にエラーが発生しました: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
		}

	}
}
