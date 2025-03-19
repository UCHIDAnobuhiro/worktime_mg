package com.example.attendance.controller;

import java.time.LocalTime;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@PostMapping("worktime/update")
	public String updateWorktime(@Valid Stamp stamp, BindingResult bindingResult, Model model,
			RedirectAttributes redirectAttributes) {

		// 何も入力されてない場合はリロード
		if (stamp.getStart_time() == null && stamp.getEnd_time() == null) {
			redirectAttributes.addFlashAttribute("editErrorMsg",
					"時間を入力してください");
			redirectAttributes.addFlashAttribute("stamp", stamp);
			return "redirect:/worktime/edit";
		}

		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		Stamp existingStamp = stampRepository.findByUserAndDay(loggedInUser, stamp.getDay());
		stamp.setUser(loggedInUser);

		//更新時データの入り替え判断が必要、existingStamp == nullの場合は挿入id不要
		if (existingStamp != null) {
			//更新時はid必須
			stamp.setId(existingStamp.getId());

			//既存の打刻データをもとに打刻時間制限の設定、既存の打刻データがない場合は、デフォルト0時と23:59分に設定
			LocalTime existingStartTimeLocal = existingStamp.getStart_time() != null
					? existingStamp.getStart_time().toLocalTime()
					: LocalTime.of(0, 0, 0);

			LocalTime existingEndTimeLocal = existingStamp.getEnd_time() != null
					? existingStamp.getEnd_time().toLocalTime()
					: LocalTime.of(23, 59, 0);

			//退勤打刻だけの場合
			if (stamp.getStart_time() == null) {

				//既存の開始打刻がある場合に制限を加え、
				//既存の開始打刻がない場合（既存の退勤打刻だけあって、その退勤打刻を編集）は制限不要。
				if (stamp.getEnd_time().toLocalTime().isAfter(existingStartTimeLocal)) {
					stamp.setStart_time(existingStamp.getStart_time());
				} else {

					redirectAttributes.addFlashAttribute("editErrorMsg",
							"既存の開始打刻:" + existingStartTimeLocal + "より遅い時間を入力してください。");
					redirectAttributes.addFlashAttribute("stamp", stamp);
					return "redirect:/worktime/edit";
				}
			}

			//開始打刻だけの場合
			if (stamp.getEnd_time() == null) {

				//既存の退勤打刻がある場合に制限を加え、既存の退勤打刻がない場合は制限不要。
				if (stamp.getStart_time().toLocalTime().isBefore(existingEndTimeLocal)) {
					stamp.setEnd_time(existingStamp.getEnd_time());
				} else {
					redirectAttributes.addFlashAttribute("editErrorMsg",
							"既存の終了打刻:" + existingEndTimeLocal + "より早い時間を入力してください。");
					redirectAttributes.addFlashAttribute("stamp", stamp);
					return "redirect:/worktime/edit";
				}
			}
		}
		// Stamp を更新
		stampService.saveOrUpdateStamp(stamp);

		//user+monthと一致してるものだけを更新
		calculatorService.updateCalculator(loggedInUser, stamp.getMonth());
		return "redirect:/worktime/home";
	}
}
