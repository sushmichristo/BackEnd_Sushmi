package com.workplace.employee.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.workplace.employee.model.Employee;
import com.workplace.employee.service.EmployeeService;
import com.workplace.employee.util.Validate;

@RestController
@CrossOrigin
public class EmployeeController {

	private Logger log = LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	private EmployeeService employeeService;

	@GetMapping("/getAllEmployees")
	public List<Employee> getAllEmployees() {
		return this.employeeService.getAllEmployees();
	}

	@GetMapping("/getEmployee/{id}")
	public Employee getEmployee(@PathVariable int id) {
		return this.employeeService.getEmployee(id);
	}
	
	@GetMapping("/searchEmployee/{name}")
	public Employee getEmployeeByName(@PathVariable String nmae) {
		return this.employeeService.getEmployeeByName(nmae);
	}

	@PostMapping("/addEmployee")
	public @ResponseBody Map<String, String> addEmployee(@RequestBody Employee employee) {
		Validate.notNull(employee);
		final Map<String, String> resultMap = new HashMap<>();
		final List<Employee> existingEmployees = this.employeeService.getAllEmployees();
		if (existingEmployees!= null && existingEmployees.isEmpty()) {
			this.employeeService.saveEmployee(employee);
			resultMap.put("success", "Emplyee has successfully added");
			return resultMap;
		} else {
			final List<Employee> duplicateList =	existingEmployees.stream().filter(existing -> existing.getName().equals(employee.getName())&&
					existing.getDesignation().equalsIgnoreCase(employee.getDesignation())
					&&existing.getSalary()==employee.getSalary())
			.collect(Collectors.toList());
			if(duplicateList.isEmpty()) {
				this.employeeService.saveEmployee(employee);
				resultMap.put("success", "Emplyee has successfully added");
				return resultMap;
			}
		}
		return resultMap;
	}

	@PutMapping("/editEmployee/{id}")
	public @ResponseBody String editEmployee(@PathVariable int id, @RequestBody Employee employee) {
		Validate.notNull(employee);
		this.employeeService.saveEmployee(employee);
		return "Employee has successfully updated";
	}

	@DeleteMapping("/deleteEmployee/{id}")
	public void deleteEmployee(@PathVariable int id) {
		this.employeeService.deleteEmployee(id);
	}
}
