package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.attendance.model.Stamp;

@Controller
public class EditController {

	@GetMapping("/worktime/edit")
	public String editWorktime() {
		return "edit";
	}

	@PostMapping("worktime/update")
	public String updateWorktime(@ModelAttribute Stamp stamp, Model model) {
		return "redirect:/worktime/home";
	}
}
