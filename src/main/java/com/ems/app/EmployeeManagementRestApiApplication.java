package com.ems.app; // Declares the package for this class

import org.springframework.boot.SpringApplication; // Class to bootstrap and launch the Spring application
import org.springframework.boot.autoconfigure.SpringBootApplication; // Annotation to enable auto-configuration, component scanning, and configuration

@SpringBootApplication // Combines @Configuration, @EnableAutoConfiguration, and @ComponentScan
public class EmployeeManagementRestApiApplication { // Main application class

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementRestApiApplication.class, args); // Launches the Spring Boot application
	}

}
