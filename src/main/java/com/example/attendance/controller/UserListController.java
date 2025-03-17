package com.example.attendance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.attendance.dto.UserWorkDataDTO;
import com.example.attendance.model.Users;
import com.example.attendance.service.UsersService;

@Controller
public class UserListController {
	@Autowired
	private UsersService usersService;

	@GetMapping("/worktime/user-list")
	public String userList(Model model) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ユーザー情報を Model に追加
		model.addAttribute("user", loggedInUser);

		// 全ユーザーの勤務データを取得
		List<UserWorkDataDTO> userWorkDataList = usersService.getUsersWithWorkData();
		model.addAttribute("userWorkDataList", userWorkDataList);

		return "user-list";
	}
}
