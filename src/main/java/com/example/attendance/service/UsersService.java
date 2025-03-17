package com.example.attendance.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.attendance.dto.UserWorkDataDTO;
import com.example.attendance.model.Users;
import com.example.attendance.repository.UsersRepository;

@Service
public class UsersService {
	private final UsersRepository usersRepository;

	public UsersService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	public Users getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof Users) {
			return (Users) authentication.getPrincipal();
		}
		return null;
	}

	public List<UserWorkDataDTO> getUsersWithWorkData() {
		return usersRepository.getUsersWithWorkData();
	}
}
