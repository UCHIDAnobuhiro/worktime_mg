package com.example.attendance.model;

import java.sql.Date;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

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
@Table(name = "stamp")
@Getter
@Setter
public class Stamp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Time start_time;

	@Column(nullable = false)
	private Time end_time;

	@Column(nullable = false)
	private Date day;

	@Column(nullable = false)
	private Long user_id;

	public String getHoursWorked() {
		if (end_time == null || start_time == null) {
			return "null"; // 如果 end_time 为 null，返回 null
		}

		long durationInMillis = end_time.getTime() - start_time.getTime();
		long durationInSeconds = durationInMillis / 1000; // 毫秒转换为秒
		long hours = TimeUnit.SECONDS.toHours(durationInSeconds); // 获取小时
		long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) - TimeUnit.HOURS.toMinutes(hours); // 获取分钟
		long seconds = durationInSeconds - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(durationInSeconds)); // 获取秒数

		return String.format("%02d:%02d:%02d", hours, minutes, seconds); // 格式化为 "hh:mm:ss"
	}
}
