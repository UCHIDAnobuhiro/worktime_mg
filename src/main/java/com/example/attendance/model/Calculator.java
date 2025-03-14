package com.example.attendance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false) // Usersテーブルのidを参照する外部キー
	private Users user; // `Users`エンティティとの関連付け

}
