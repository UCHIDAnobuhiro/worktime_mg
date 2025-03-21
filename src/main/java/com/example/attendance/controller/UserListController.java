package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.service.UserListService;

@Controller
public class UserListController {
	private final UserListService userListService;

	public UserListController(UserListService userListService) {
		this.userListService = userListService;
	}

	@GetMapping("/worktime/user-list")
	public String userList(@RequestParam(required = false) Integer month, Model model) {
		try {
			userListService.prepareUserListPage(month, model);
			return "user-list";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "ユーザーリストの取得中にエラーが発生しました: " + e.getMessage());
			return "error";
		}
	}
}
