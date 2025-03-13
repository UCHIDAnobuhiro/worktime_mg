package com.example.attendance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Stamp;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {

	List<Stamp> findByUserId(Long userId);
}
