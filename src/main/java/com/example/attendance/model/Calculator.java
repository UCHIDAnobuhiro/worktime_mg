package com.example.attendance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "calculator")
@Getter
@Setter
public class Calculator {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "work_time_month", nullable = false)
	private String workTimeMonth;

	@Column(name = "work_days_month", nullable = false)
	private Integer workDaysMonth;

	@Column(nullable = false)
	private Integer month;

	@Column(name = "user_id", nullable = false)
	private Long userId;

}
