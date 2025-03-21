package com.example.attendance.service;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;
import com.example.attendance.repository.StampRepository;

@Service
public class StampService {

	private final StampRepository stampRepository;
	private final CalculatorService calculatorService;
	private final UsersService usersService;

	public StampService(StampRepository stampRepository, CalculatorService calculatorService,
			UsersService usersService) {
		this.stampRepository = stampRepository;
		this.calculatorService = calculatorService;
		this.usersService = usersService;
	}

	// 打刻画面に表示する初期データ（ログインユーザー情報、現在の日付と時刻）を Model にセットします。
	public void prepareStampData(Model model) {
		// ログイン中のユーザを取得
		Users loggedInUser = usersService.getLoggedInUserOrThrow();
		model.addAttribute("user", loggedInUser);

		// 現在日時を取得
		LocalDateTime now = LocalDateTime.now();

		// 日付と時刻のフォーマットを定義（例：2025-03-21, 14:30:00）
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

		// フォーマット済みの現在日付・時刻をModelに追加
		model.addAttribute("nowDate", now.format(dateFormatter));
		model.addAttribute("nowTime", now.format(timeFormatter));

	}

	public String handleStampSubmission(Boolean isClockIn, RedirectAttributes redirectAttributes) {
		//　出勤選択がなければエラー
		if (isClockIn == null) {
			redirectAttributes.addFlashAttribute("stampErrorMsg", "出退勤を選択してください");
			return "redirect:/worktime/stamp";
		}

		// ログインユーザー、日付、現在時刻の取得
		Users user = usersService.getLoggedInUserOrThrow();
		LocalDate today = LocalDate.now();
		Time nowTime = Time.valueOf(LocalTime.now());
		Date day = Date.valueOf(today);
		boolean isClockOut = false;

		// 既存の打刻データがあるかチェック（今日の分）
		Stamp existing = stampRepository.findByUserAndDay(user, day);

		// 保存・更新対象のStampオブジェクトを作成
		Stamp stamp = new Stamp();
		stamp.setUser(user);
		stamp.setDay(day);

		if (existing != null) {
			// 上書き保存のため、IDと既存の打刻情報をコピー
			stamp.setId(existing.getId());
			stamp.setStart_time(existing.getStart_time());
			stamp.setEnd_time(existing.getEnd_time());

			// 既存データがある場合の出勤・退勤ロジック
			String redirect = handleExistingStamp(isClockIn, existing, stamp, nowTime, redirectAttributes);
			if (redirect != null) {
				return redirect;
			}

			if (!isClockIn) {
				isClockOut = true;
			}
		} else {
			// 新規打刻（初回出勤 or 退勤）
			if (isClockIn) {
				stamp.setStart_time(nowTime);
			} else {
				stamp.setEnd_time(nowTime);
				isClockOut = true;
			}
		}

		// 打刻を保存
		stampRepository.save(stamp);

		// 退勤が完了した場合、勤務時間集計を更新
		if (isClockOut) {
			calculatorService.updateCalculator(user, today.getMonthValue());
		}

		return "redirect:/worktime/home";
	}

	private String handleExistingStamp(boolean isClockIn, Stamp existing, Stamp target, Time nowTime,
			RedirectAttributes redirectAttributes) {
		if (isClockIn) {
			// すでに出勤済みならエラー
			if (existing.getStart_time() != null) {
				redirectAttributes.addFlashAttribute("stampErrorMsg", "すでに出勤した");
				return "redirect:/worktime/stamp";

				// 退勤だけ済んでいる場合は出勤打刻不可
			} else if (existing.getEnd_time() != null) {
				redirectAttributes.addFlashAttribute("stampErrorMsg", "すでに退勤したので出勤打刻はできません");
				return "redirect:/worktime/stamp";
				// まだどちらも打刻されていなければ出勤打刻として記録
			} else {
				target.setStart_time(nowTime);
			}
		} else {
			// すでに退勤済みならエラー
			if (existing.getEnd_time() != null) {
				redirectAttributes.addFlashAttribute("stampErrorMsg", "すでに退勤した");
				return "redirect:/worktime/stamp";
				// 退勤未済みなら退勤打刻として記録
			} else {
				target.setEnd_time(nowTime);
			}
		}
		return null;
	}

}