package com.example.attendance.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.attendance.exception.UserRegistrationException;
import com.example.attendance.model.Department;
import com.example.attendance.model.Users;
import com.example.attendance.service.DepartmentService;
import com.example.attendance.service.UsersService;

@Controller
public class SignupController {
	private final UsersService usersService;
	private final DepartmentService departmentService;

	@Autowired
	public SignupController(UsersService usersService, DepartmentService departmentService) {
		this.usersService = usersService;
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
		model.addAttribute("departments", departmentService.getAllDepartments());

		if (result.hasErrors()) {
			return "signup";
		}

		try {
			// serviceで処理を行う
			usersService.registerUser(user);

			// 成功メッセージをフラッシュ属性で送る
			redirectAttributes.addFlashAttribute("success", "登録が完了しました！ログインしてください。");

			// ログインページにリダイレクト
			return "redirect:/login";

		} catch (UserRegistrationException e) {
			result.rejectValue(e.getFieldName(), "error.user", e.getMessage());
			return "signup";
		}

	}
}