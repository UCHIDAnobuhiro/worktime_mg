package com.example.attendance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.attendance.model.Users;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Optional<Users> findByMail(String mail);

}
