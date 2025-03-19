package com.example.attendance.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.example.attendance.dto.UserWorkDataDTO;
import com.example.attendance.exception.UserRegistrationException;
import com.example.attendance.model.Department;
import com.example.attendance.model.Users;
import com.example.attendance.repository.UsersRepository;

@Service
public class UsersService {
	private final UsersRepository usersRepository;
	private final DepartmentService departmentService;
	private final PasswordEncoder passwordEncoder;

	public UsersService(UsersRepository usersRepository, DepartmentService departmentService,
			PasswordEncoder passwordEncoder) {
		this.usersRepository = usersRepository;
		this.departmentService = departmentService;
		this.passwordEncoder = passwordEncoder;
	}

	public Users getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.getPrincipal() instanceof Users) {
			return (Users) authentication.getPrincipal();
		}
		return null;
	}
  
  public List<UserWorkDataDTO> getUsersWithWorkData(Integer month) {
		return usersRepository.getUsersWithWorkData(month);
	}

	@Transactional
	public void registerUser(Users user) {
		if (user.getName() == null || user.getName().isBlank()) {
			throw new UserRegistrationException("name", "名前を入力してください");
		}

		if (user.getMail() == null || user.getMail().isBlank()) {
			throw new UserRegistrationException("mail", "メールアドレスを入力してください");
		}

		if (usersRepository.findByMail(user.getMail()).isPresent()) {
			throw new UserRegistrationException("mail", "このメールアドレスは既に登録されています");
		}

		if (user.getPassword() == null || user.getConfirmPassword() == null) {
			throw new UserRegistrationException("password", "パスワードを入力してください");
		}

		if (!user.getPassword().equals(user.getConfirmPassword())) {
			throw new UserRegistrationException("confirmPassword", "パスワードが一致しません");
		}

		if (user.getDepartment() == null || user.getDepartment().getId() == null) {
			throw new UserRegistrationException("department", "有効な部署を選択してください");
		}

		Optional<Department> departmentOpt = departmentService.getDepartmentById(user.getDepartment().getId());
		if (departmentOpt.isEmpty()) {
			throw new UserRegistrationException("department", "選択された部署が無効です");
		}

		user.setDepartment(departmentOpt.get());

		// ユーザー情報をセット
		user.setCreateDate(LocalDateTime.now());
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// ユーザーをデータベースに保存
		usersRepository.save(user);
    
  }

}
