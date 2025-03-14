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
import com.example.attendance.repository.StampRepository;
import com.example.attendance.service.StampService;

@Controller
public class StampController {

	@Autowired
	private StampRepository stampRepository;
	@Autowired
	private StampService stampService;

	@GetMapping("/worktime/stamp")
	public String showStamp(Model model) {
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
		Stamp nowStamp = new Stamp();
		LocalTime localTime = LocalTime.now();
		LocalDate localDate = LocalDate.now();
		Time nowTime = Time.valueOf(localTime);
		Date day = Date.valueOf(localDate);

		Long userId = 2L;
		Stamp existingStamp = stampRepository.findByUserIdAndDay(userId, day);

		nowStamp.setUserId(userId);
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
		return "home";
	}

}
