package com.example.attendance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.attendance.dto.UserWorkDataDTO;
import com.example.attendance.model.Users;

@Service
public class UserListService {

	private final UsersService usersService;

	public UserListService(UsersService usersService) {
		this.usersService = usersService;
	}

	public void prepareUserListPage(Integer month, Model model) {
		// 月が未指定なら現在の月を使用
		if (month == null) {
			month = LocalDate.now().getMonthValue();
		}

		// ログインユーザー情報の取得
		Users loggedInUser = usersService.getLoggedInUserOrThrow();
		model.addAttribute("user", loggedInUser);

		// ユーザーごとの勤務情報取得
		List<UserWorkDataDTO> userWorkDataList = usersService.getUsersWithWorkData(month);
		model.addAttribute("userWorkDataList", userWorkDataList);

		// 選択された月を表示用に追加
		model.addAttribute("selectedMonth", month);
	}

}
