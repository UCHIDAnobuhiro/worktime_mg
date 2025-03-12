package com.example.attendance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.attendance.model.Stamp;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {
}
