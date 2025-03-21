package com.example.attendance.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.attendance.model.Department;
import com.example.attendance.repository.DepartmentRepository;

@Service
public class DepartmentService {

	private final DepartmentRepository departmentRepository;

	public DepartmentService(DepartmentRepository departmentRepository) {
		this.departmentRepository = departmentRepository;
	}

	public List<Department> getAllDepartments() {
		return departmentRepository.findAll();
	}

	public Optional<Department> getDepartmentById(Long id) {
		return departmentRepository.findById(id);
	}
}