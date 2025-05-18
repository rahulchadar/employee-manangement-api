package com.ems.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner; // Allows code to run after Spring Boot starts
import org.springframework.security.crypto.password.PasswordEncoder; // For securely encoding passwords
import org.springframework.stereotype.Component;

import com.ems.app.entity.RoleType; // Enum for user roles (e.g., ADMIN, EMPLOYEE, CLIENT)
import com.ems.app.entity.User; // Entity representing the user table
import com.ems.app.repository.UserRepository; // Repository for accessing user data

@Component // Marks this class as a Spring-managed component (bean)
public class AdminDataInitializer implements CommandLineRunner { // Runs on application startup

    @Autowired
    private UserRepository userRepository; // Injects the UserRepository bean

    @Autowired
    private PasswordEncoder passwordEncoder; // Injects the PasswordEncoder bean for hashing passwords

    @Override
    public void run(String... args) {
        String defaultEmail = "admin@gmail.com"; // Email to check if default admin already exists

        // If no user with the default email exists in the database
        if (!userRepository.findByEmail(defaultEmail).isPresent()) {
            User admin = new User(); // Create a new User object
            admin.setEmail("admin@gmail.com"); // Set the email
            admin.setPassword(passwordEncoder.encode("admin123")); // Hash and set the password
            admin.setRole(RoleType.ADMIN); // Assign the ADMIN role
            userRepository.save(admin); // Save the admin user to the database
            System.out.println("Default admin created."); // Log message to indicate creation
        }
    }
}
