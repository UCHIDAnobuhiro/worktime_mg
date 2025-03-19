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
import com.example.attendance.repository.StampRepository;
import com.example.attendance.service.CalculatorService;
import com.example.attendance.service.StampService;
import com.example.attendance.service.UsersService;

@Controller
public class EditController {

	@Autowired
	private StampService stampService;

	@Autowired
	private UsersService usersService;

	@Autowired
	private CalculatorService calculatorService;

	@Autowired
	private StampRepository stampRepository;

	@GetMapping("/worktime/edit")
	public String editWorktime(Model model) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ユーザー情報を Model に追加
		model.addAttribute("user", loggedInUser);

		return "edit";
	}

	//	@SuppressWarnings("null")
	@PostMapping("worktime/update")
	public String updateWorktime(@Valid Stamp stamp, BindingResult bindingResult) {

		// 何も入力されてない場合はリロード
		if (stamp.getStart_time() == null && stamp.getEnd_time() == null) {
			return "redirect:/worktime/edit";
		}

		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		Stamp existingStamp = stampRepository.findByUserAndDay(loggedInUser, stamp.getDay());
		stamp.setUser(loggedInUser);

		if (existingStamp != null) {
			//更新時はid必須
			stamp.setId(existingStamp.getId());
			if (stamp.getStart_time() == null) {
				stamp.setStart_time(existingStamp.getStart_time());
			}
			if (stamp.getEnd_time() == null) {
				stamp.setEnd_time(existingStamp.getEnd_time());
			}
		}

		// Stamp を更新
		stampService.saveOrUpdateStamp(stamp);

		//user+monthと一致してるものだけを更新
		calculatorService.updateCalculator(loggedInUser, stamp.getMonth());
		return "redirect:/worktime/home";
	}
}
