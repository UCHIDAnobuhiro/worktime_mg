package com.example.attendance.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
//import jakarta.persistence.Transient;
import jakarta.persistence.Transient;

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

	@Transient
	private Integer month;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	//	dayでmonthを取得
	public Integer getMonth() {
		if (day != null) {
			LocalDate localDate = day.toLocalDate();
			System.out.println(localDate.getMonthValue());
			return localDate.getMonthValue();
		}
		return null;
	}

	//毎日の勤務時間を計算
	public String getHoursWorked() {
		//どちらかがnullの場合00:00:00に変換
		if (end_time == null || start_time == null) {
			return "00:00:00";
		}
		long durationInMillis = end_time.getTime() - start_time.getTime();
		long durationInSeconds = durationInMillis / 1000; //変形
		long hours = TimeUnit.SECONDS.toHours(durationInSeconds);//Hour
		long minutes = TimeUnit.SECONDS.toMinutes(durationInSeconds) - TimeUnit.HOURS.toMinutes(hours); //Minutes
		long seconds = durationInSeconds - TimeUnit.MINUTES.toSeconds(TimeUnit.SECONDS.toMinutes(durationInSeconds)); //seconds

		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
	}
}
