package com.example.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.attendance.model.Calculator;
import com.example.attendance.model.Stamp;
import com.example.attendance.repository.CalculatorRepository;
import com.example.attendance.repository.StampRepository;
import com.example.attendance.service.HomeService;

@Controller
public class HomeController {

	@Autowired
	private StampRepository stampRepository;

	@Autowired
	private CalculatorRepository calculatorRepository;

	@Autowired
	private HomeService homeService;

	@GetMapping("/worktime/home")
	public String showHome(Model model) {
		List<Stamp> stamps = homeService.getAllStamps();
		List<Calculator> calculators = homeService.getAllCalculator();
		model.addAttribute("stamps", stamps);
		model.addAttribute("calculators", calculators);
		return "home";
	}
}
