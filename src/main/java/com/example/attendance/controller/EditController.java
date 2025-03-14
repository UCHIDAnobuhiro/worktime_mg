package com.example.attendance.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;
import com.example.attendance.service.StampService;
import com.example.attendance.service.UsersService;

@Controller
public class EditController {

	@Autowired
	private StampService stampService;

	@Autowired
	private UsersService usersService;

	@GetMapping("/worktime/edit")
	public String editWorktime(Model model) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ユーザー情報を Model に追加
		model.addAttribute("user", loggedInUser);

		return "edit";
	}

	@PostMapping("worktime/update")
	public String updateWorktime(@Valid Stamp stamp, BindingResult bindingResult) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ログインユーザーをセット
		stamp.setUser(loggedInUser);

		// Stamp を更新
		stampService.saveOrUpdateStamp(stamp);
		return "redirect:/worktime/home";
	}
}
