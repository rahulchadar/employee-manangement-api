package com.ems.app.controller; // Defines the controller's package

import org.springframework.web.bind.annotation.RestController; // Marks the class as a REST controller

import com.ems.app.dto.APIResponse; // Wrapper class for standard API responses
import com.ems.app.entity.Employee; // Entity class for Employee
import com.ems.app.entity.Project; // Entity class for Project
import com.ems.app.service.EmployeeService; // Service layer to handle employee-related logic

import org.springframework.beans.factory.annotation.Autowired; // Enables automatic dependency injection
import org.springframework.security.core.Authentication; // Represents the currently authenticated user
import org.springframework.web.bind.annotation.GetMapping; // Handles HTTP GET requests
import org.springframework.web.bind.annotation.RequestMapping; // Maps base URL for the controller

@RestController // Marks this class as a REST controller (all methods return JSON/XML by default)
@RequestMapping("/employee") // Base URL path for all endpoints in this controller
public class EmployeeController {

	@Autowired
	EmployeeService employeeService; // Injects the EmployeeService bean to access business logic
	
	@GetMapping("/myDetails") // Handles GET requests to /employee/myDetails
	public APIResponse<Employee> getEmployee(Authentication authentication) {
		String email = authentication.getName(); // Gets the currently authenticated user's email
		
		return employeeService.getEmployeeByEmail(email); // Fetches employee details by email
	}
	
	@GetMapping("/myProject") // Handles GET requests to /employee/myProject
	public APIResponse<Project> getEmployeeProject(Authentication authentication) {
		String email = authentication.getName(); // Gets the currently authenticated user's email
		
		return employeeService.getProjectByEmployeeEmail(email); // Fetches the project assigned to the employee
	}
	
}
