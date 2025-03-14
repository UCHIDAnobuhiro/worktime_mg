package com.example.attendance.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.attendance.repository.UsersRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	private final UsersRepository usersRepository;

	public CustomUserDetailsService(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usersRepository.findByMail(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
	}
}
