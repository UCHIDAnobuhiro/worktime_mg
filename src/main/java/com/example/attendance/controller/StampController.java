package com.example.attendance.controller;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;
import com.example.attendance.repository.StampRepository;
import com.example.attendance.service.CalculatorService;
import com.example.attendance.service.StampService;
import com.example.attendance.service.UsersService;

@Controller
public class StampController {

	@Autowired
	private StampRepository stampRepository;
	@Autowired
	private StampService stampService;
	@Autowired
	private CalculatorService calculatorService;
	@Autowired
	private UsersService usersService;

	@GetMapping("/worktime/stamp")
	public String showStamp(Model model) {
		// 現在ログインしているユーザーを取得
		Users loggedInUser = usersService.getLoggedInUser();

		// ユーザー情報を Model に追加
		model.addAttribute("user", loggedInUser);

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		model.addAttribute("nowDate", now.format(dateFormatter));
		model.addAttribute("nowTime", now.format(timeFormatter));
		return "stamp";
	}

	@PostMapping("/worktime/stamp")
	public String insertStamp(@RequestParam(value = "isClockIn", required = false) Boolean isClockIn,
			RedirectAttributes redirectAttributes) {

		if (isClockIn == null) {
			redirectAttributes.addFlashAttribute("stampErrorMsg", "出退勤を選択してください");
			return "redirect:/worktime/stamp";
		}

		// 現在ログインしているユーザを取得
		Users loggedInUser = usersService.getLoggedInUser();

		//現在の日付と時間を取得
		LocalTime localTime = LocalTime.now();
		LocalDate localDate = LocalDate.now();
		Time nowTime = Time.valueOf(localTime);
		Date day = Date.valueOf(localDate);

		Boolean isClockOut = false;
		Stamp existingStamp = stampRepository.findByUserAndDay(loggedInUser, day);

		Stamp nowStamp = new Stamp();
		nowStamp.setUser(loggedInUser);
		nowStamp.setDay(day);

		if (existingStamp != null) {
			if (existingStamp.getDay().equals(day)) {
				nowStamp.setId(existingStamp.getId());
			}
			if (isClockIn) {
				nowStamp.setEnd_time(existingStamp.getEnd_time());
				if (existingStamp.getStart_time() == null) {
					nowStamp.setStart_time(nowTime);
				} else {
					nowStamp.setStart_time(existingStamp.getStart_time());
					redirectAttributes.addFlashAttribute("stampErrorMsg", "すでに出勤した");
					return "redirect:/worktime/stamp";
				}
			} else {
				nowStamp.setStart_time(existingStamp.getStart_time());
				if (existingStamp.getEnd_time() == null) {
					nowStamp.setEnd_time(nowTime);
					isClockOut = true;
				} else {
					nowStamp.setEnd_time(existingStamp.getEnd_time());
					redirectAttributes.addFlashAttribute("stampErrorMsg", "すでに退勤した");
					return "redirect:/worktime/stamp";
				}
			}
		} else {
			if (isClockIn) {
				nowStamp.setStart_time(nowTime);
			} else {
				nowStamp.setEnd_time(nowTime);
			}
		}
		stampService.saveOrUpdateStamp(nowStamp);
		if (isClockOut) {
			calculatorService.updateCalculator(loggedInUser, nowStamp.getMonth());
		}
		return "redirect:/worktime/home";
	}

}
