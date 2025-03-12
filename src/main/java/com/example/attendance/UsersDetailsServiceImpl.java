package com.example.attendance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.attendance.model.Users;
import com.example.attendance.repository.UsersRepository;

@Service
public class UsersDetailsServiceImpl implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(UsersDetailsServiceImpl.class);

	private final UsersRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public UsersDetailsServiceImpl(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		logger.info("Authenticating user: {}", mail);

		Users user = userRepository.findByMail(mail)
				.orElseThrow(() -> new UsernameNotFoundException("Email not found: " + mail));

		logger.info("Authentication successful: {}", user.getMail());

		return User.withUsername(user.getMail())
				.password(user.getPassword()) // すでにハッシュ化されている前提
				.authorities("ROLE_USER")
				.build();
	}
}