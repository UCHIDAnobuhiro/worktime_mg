package com.example.attendance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserListController {

	@GetMapping("/worktime/user-list")
	public String userList() {
		return "user-list";
	}
}
