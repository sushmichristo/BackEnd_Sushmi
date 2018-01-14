package com.workplace.employee.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.workplace.employee.model.Employee;
import com.workplace.employee.repository.EmployeeRepository;

@Service
public class EmployeeService {
	
	@Autowired
	private EmployeeRepository employeeRepository;

	public List<Employee> getAllEmployees() {
		final List<Employee> employees = new ArrayList<>();
		 this.employeeRepository.findAll().forEach(employees::add);
		 return employees;
	}

	public Employee getEmployee(int id) {
		return this.employeeRepository.findOne(id);
	}
	
	public void saveEmployee(Employee employee) {
		this.employeeRepository.save(employee);
	}

	public void deleteEmployee(int id) {
		this.employeeRepository.delete(id);
	}

	public Employee getEmployeeByName(String name) {
		return this.employeeRepository.findByName(name);
	}

}
