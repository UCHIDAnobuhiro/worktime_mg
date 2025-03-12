package com.example.attendance.model;

import java.time.Duration;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import jakarta.persistence.Transient;

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

	@Column(nullable = false)
	private Duration work_time_month; // 使用 Duration 类型

	@Column(nullable = false)
	private Integer work_days_month;

	@Column(nullable = false)
	private Integer month;

	@Column(nullable = false)
	private Long user_id;

}
