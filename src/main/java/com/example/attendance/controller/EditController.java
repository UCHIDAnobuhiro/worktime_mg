package com.example.attendance.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;
import com.example.attendance.service.EditService;
import com.example.attendance.service.UsersService;

@Controller
public class EditController {

	private final EditService editService;
	private final UsersService usersService;

	public EditController(EditService editService, UsersService usersService) {
		this.editService = editService;
		this.usersService = usersService;
	}

	@GetMapping("/worktime/edit")
	public String editWorktime(Model model) {
		try {
			// 現在ログインしているユーザーを取得
			Users loggedInUser = usersService.getLoggedInUser();
			model.addAttribute("user", loggedInUser);
			return "edit";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "編集画面の表示中にエラーが発生しました: " + e.getMessage());
			return "error"; // error.html を表示
		}
	}

	@PostMapping("/worktime/update")
	public String updateWorktime(@Valid Stamp stamp, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Users loggedInUser = usersService.getLoggedInUser();
			return editService.updateStamp(stamp, redirectAttributes, loggedInUser);
		} catch (Exception e) {
			// エラー発生時、エラーメッセージを表示用に渡す
			model.addAttribute("errorMessage", "勤務時間の更新中にエラーが発生しました: " + e.getMessage());
			return "error"; // error.html に遷移
		}
	}
}
