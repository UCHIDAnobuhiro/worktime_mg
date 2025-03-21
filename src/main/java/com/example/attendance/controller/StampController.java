package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.service.StampService;

@Controller
public class StampController {

	private final StampService stampService;

	public StampController(StampService stampService) {
		this.stampService = stampService;
	}

	@GetMapping("/worktime/stamp")
	public String showStamp(Model model) {
		try {
			stampService.prepareStampData(model);
			return "stamp";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "データ取得中にエラーが発生しました: " + e.getMessage());
			return "error";
		}
	}

	@PostMapping("/worktime/stamp")
	public String insertStamp(@RequestParam(required = false) Boolean isClockIn,
			RedirectAttributes redirectAttributes) {
		return stampService.handleStampSubmission(isClockIn, redirectAttributes);
	}

}
