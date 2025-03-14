package com.example.attendance.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Stamp;
import com.example.attendance.model.Users;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {
	List<Stamp> findByUser(Users user);

	Stamp findByUserAndDay(Users user, Date day);
}
