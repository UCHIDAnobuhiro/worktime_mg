package com.example.attendance.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StampController {

	@GetMapping("/worktime/stamp")
	public String showStamp(Model model) {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		model.addAttribute("nowDate", now.format(dateFormatter));
		model.addAttribute("nowTime", now.format(timeFormatter));
		return "stamp";
	}

}
