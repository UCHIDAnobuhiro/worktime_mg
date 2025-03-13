package com.example.attendance.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.model.Department;
import com.example.attendance.model.Users;
import com.example.attendance.repository.UsersRepository;
import com.example.attendance.service.DepartmentService;

@Controller
public class SignupController {
	private final UsersRepository usersRepository;
	private final PasswordEncoder passwordEncoder;
	private final DepartmentService departmentService;

	@Autowired
	public SignupController(UsersRepository usersRepository, PasswordEncoder passwordEncoder,
			DepartmentService departmentService) {
		this.usersRepository = usersRepository;
		this.passwordEncoder = passwordEncoder;
		this.departmentService = departmentService;
	}

	@GetMapping("/signup")
	public String showSignupForm(Model model) {
		model.addAttribute("user", new Users());
		List<Department> departments = departmentService.getAllDepartments();
		model.addAttribute("departments", departments);
		return "signup";
	}

	@PostMapping("/signup")
	public String processSignup(@ModelAttribute("user") @Valid Users user, BindingResult result,
			RedirectAttributes redirectAttributes, Model model) {

		// 部署リストを取得（エラー時の再表示用）
		List<Department> departments = departmentService.getAllDepartments();
		model.addAttribute("departments", departments);

		if (result.hasErrors()) {
			System.out.println("バリデーションエラー: " + result.getAllErrors());
			return "signup";
		}

		// 既存のメールアドレスの確認
		Optional<Users> existingUser = usersRepository.findByMail(user.getMail());
		if (existingUser.isPresent()) {
			result.rejectValue("mail", "error.user", "このメールアドレスは既に登録されています");
			return "signup";
		}

		// パスワードの一致チェック JSでも実装しているが念の為バックでも実装
		if (!user.getPassword().equals(user.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "error.user", "パスワードが一致しません");
			return "signup";
		}

		// 選択された `departmentId` から `Department` エンティティを取得
		Optional<Department> departmentOpt = departmentService.getDepartmentById(user.getDepartment().getId());
		if (departmentOpt.isEmpty()) {
			result.rejectValue("department", "error.user", "選択された部署が無効です");
			return "signup";
		}
		user.setDepartment(departmentOpt.get()); // Departmentエンティティをセット

		// 日付のデータ取得
		user.setCreateDate(LocalDateTime.now());

		// パスワードをハッシュ化
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// ユーザーをデータベースに保存
		usersRepository.save(user);

		// 成功メッセージをフラッシュ属性で送る
		redirectAttributes.addFlashAttribute("success", "登録が完了しました！ログインしてください。");

		// ログインページにリダイレクト
		return "redirect:/login";
	}
}