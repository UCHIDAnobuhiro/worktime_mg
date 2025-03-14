package com.example.attendance.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.attendance.model.Stamp;
import com.example.attendance.service.StampService;

@Controller
public class EditController {

	@Autowired
	private StampService stampService;

	@GetMapping("/worktime/edit")
	public String editWorktime() {
		return "edit";
	}

	@PostMapping("worktime/update")
	public String updateWorktime(@Valid Stamp stamp, BindingResult bindingResult) {
		Long userId = 2L;
		stamp.setUserId(userId);
		stampService.saveOrUpdateStamp(stamp);
		return "redirect:/worktime/home";
	}
}
