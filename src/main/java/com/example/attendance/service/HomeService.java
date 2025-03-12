package com.example.attendance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.attendance.model.Calculator;
import com.example.attendance.model.Stamp;
import com.example.attendance.repository.CalculatorRepository;
import com.example.attendance.repository.StampRepository;

@Service
public class HomeService {
	@Autowired
	private StampRepository stampRepository;

	@Autowired
	private CalculatorRepository calculatorRepository;

	public List<Stamp> getAllStamps() {
		return stampRepository.findAll();
	}

	public List<Calculator> getAllCalculator() {
		return calculatorRepository.findAll();
	}
}
