package com.example.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkTimeResponse {
	private String workTimeMonth;
	private Integer workDaysMonth;

}
