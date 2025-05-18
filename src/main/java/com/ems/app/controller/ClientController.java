package com.ems.app.controller; // Declares the controller package

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ems.app.dto.APIResponse; // Custom wrapper class for API responses
import com.ems.app.entity.Client; // Entity representing the client
import com.ems.app.entity.Project; // Entity representing the project
import com.ems.app.service.ClientService; // Service layer to handle client-related logic

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; // For dependency injection
import org.springframework.security.core.Authentication; // Provides authenticated user details
import org.springframework.web.bind.annotation.GetMapping; // Maps HTTP GET requests

@RestController // Marks this class as a REST controller (response body by default)
@RequestMapping("/client") // Base path for all endpoints in this controller
public class ClientController {

	@Autowired
	ClientService clientService; // Injects the client service bean
	
	@GetMapping("/details") // Handles GET requests to /client/details
	public APIResponse<Client> clientDetails(Authentication authentication) {
		String email = authentication.getName(); // Retrieves the email of the currently authenticated user
		
		return clientService.getClientByEmail(email); // Calls service to fetch client details by email
	}
	
	@GetMapping("/projects") // Handles GET requests to /client/projects
	public APIResponse<List<Project>> clientProjects(Authentication authentication) {
		String email = authentication.getName(); // Retrieves the email of the currently authenticated user
		
		return clientService.getClientProjectsByEmail(email); // Calls service to fetch client's projects by email
	}
	
}
