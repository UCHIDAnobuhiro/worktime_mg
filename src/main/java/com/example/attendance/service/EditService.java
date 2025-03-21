package com.example.attendance.service;

import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;
import com.example.attendance.repository.StampRepository;

@Service
public class EditService {

	public final StampRepository stampRepository;
	public final StampService stampService;
	public final CalculatorService calculatorService;

	public EditService(StampRepository stampRepository, StampService stampService, CalculatorService calculatorServic) {
		this.stampRepository = stampRepository;
		this.stampService = stampService;
		this.calculatorService = calculatorServic;
	}

	public String updateStamp(Stamp stamp, RedirectAttributes redirectAttributes, Users loggedInUser) {
		// 入力チェック：両方nullならエラー
		if (stamp.getStart_time() == null && stamp.getEnd_time() == null) {
			redirectAttributes.addFlashAttribute("editErrorMsg", "時間を入力してください");
			redirectAttributes.addFlashAttribute("stamp", stamp);
			return "redirect:/worktime/edit";
		}

		LocalTime startTime = stamp.getStart_time() != null ? stamp.getStart_time().toLocalTime() : null;
		LocalTime endTime = stamp.getEnd_time() != null ? stamp.getEnd_time().toLocalTime() : null;

		Stamp existingStamp = stampRepository.findByUserAndDay(loggedInUser, stamp.getDay());
		stamp.setUser(loggedInUser);

		if (existingStamp != null) {
			stamp.setId(existingStamp.getId());

			LocalTime existingStartTime = existingStamp.getStart_time() != null
					? existingStamp.getStart_time().toLocalTime()
					: LocalTime.of(0, 0);

			LocalTime existingEndTime = existingStamp.getEnd_time() != null
					? existingStamp.getEnd_time().toLocalTime()
					: LocalTime.of(23, 59);

			// 退勤のみ入力された場合：既存の出勤時間より後であること
			if (startTime == null && endTime != null) {
				if (existingStamp.getStart_time() != null && endTime.isAfter(existingStartTime)) {
					stamp.setStart_time(existingStamp.getStart_time());
				} else if (existingStamp.getStart_time() != null) {
					redirectAttributes.addFlashAttribute("editErrorMsg",
							"既存の開始打刻: " + existingStartTime + " より遅い時間を入力してください。");
					redirectAttributes.addFlashAttribute("stamp", stamp);
					return "redirect:/worktime/edit";
				}
			}

			// 出勤のみ入力された場合：既存の退勤時間より前であること
			if (endTime == null && startTime != null) {
				if (existingStamp.getEnd_time() != null && startTime.isBefore(existingEndTime)) {
					stamp.setEnd_time(existingStamp.getEnd_time());
				} else if (existingStamp.getEnd_time() != null) {
					redirectAttributes.addFlashAttribute("editErrorMsg",
							"既存の終了打刻: " + existingEndTime + " より早い時間を入力してください。");
					redirectAttributes.addFlashAttribute("stamp", stamp);
					return "redirect:/worktime/edit";
				}
			}
		}

		// 更新処理
		stampRepository.save(stamp);
		calculatorService.updateCalculator(loggedInUser, stamp.getMonth());

		return "redirect:/worktime/home";
	}

}
