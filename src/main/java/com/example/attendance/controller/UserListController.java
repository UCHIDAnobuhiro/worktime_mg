package com.example.attendance.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.attendance.dto.UserWorkDataDTO;
import com.example.attendance.model.Users;
import com.example.attendance.service.UsersService;

@Controller
public class UserListController {
	@Autowired
	private UsersService usersService;

	@GetMapping("/worktime/user-list")
	public String userList(@RequestParam(value = "month", required = false) Integer month, Model model) {
		// 月のデフォルト値を現在の月に設定
		if (month == null) {
			month = LocalDate.now().getMonthValue();
		}

		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ユーザー情報を Model に追加
		model.addAttribute("user", loggedInUser);

		// 全ユーザーの勤務データを取得
		List<UserWorkDataDTO> userWorkDataList = usersService.getUsersWithWorkData(month);
		model.addAttribute("userWorkDataList", userWorkDataList);

		// 選択された月を Thymeleaf に渡す
		model.addAttribute("selectedMonth", month);

		return "user-list";
	}
}
