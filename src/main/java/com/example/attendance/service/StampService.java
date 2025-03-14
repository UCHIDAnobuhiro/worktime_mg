package com.example.attendance.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.model.Stamp;
import com.example.attendance.repository.StampRepository;

@Service
public class StampService {
	@Autowired
	private StampRepository stampRepository;

	public Stamp saveOrUpdateStamp(Stamp stamp) {
		return stampRepository.save(stamp);
	}
}
