package com.example.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.attendance.dto.UserWorkDataDTO;
import com.example.attendance.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByMail(String mail);

	@Query("SELECT new com.example.attendance.dto.UserWorkDataDTO(" +
			"COALESCE(u.name, '未設定'), " +
			"COALESCE(c.workTimeMonth, '00:00:00'), " +
			"COALESCE(c.workDaysMonth, 0), " +
			"COALESCE(c.month, :month)) " + // 追加: 月の情報をDTOに含める
			"FROM Users u " +
			"LEFT JOIN Calculator c ON u.id = c.user.id " +
			"AND (c.month = :month OR c.month IS NULL)")
	List<UserWorkDataDTO> getUsersWithWorkData(@Param("month") Integer month);
}
