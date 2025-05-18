package com.ems.app; // Declares the base package

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.core.userdetails.UserDetailsService; // Custom implementation will load users from DB
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Encoder for password hashing
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain; // New way to define security filter chain
import org.springframework.security.config.Customizer; // For default basic auth config

@Configuration // Marks this class as a configuration class
@EnableWebSecurity // Enables Spring Security for the application
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService; // Custom user details service to fetch user data from DB

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Defines the encoder used to hash/check passwords
    }

    // Defines the AuthenticationManager bean (handles login authentication)
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class); // Get builder from HttpSecurity context

        authBuilder
            .userDetailsService(userDetailsService) // Use our custom user loading logic
            .passwordEncoder(passwordEncoder()); // Use the encoder to validate passwords

        return authBuilder.build(); // Build and return the AuthenticationManager
    }

    // Defines security filter chain (which URLs are secured, roles, etc.)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Disables CSRF (important for REST APIs, not needed if stateless)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN") // Only ADMIN role can access /admin/**
                .requestMatchers("/employee/**").hasRole("EMPLOYEE") // EMPLOYEE-only routes
                .requestMatchers("/client/**").hasRole("CLIENT") // CLIENT-only routes
                .anyRequest().authenticated() // All other requests must be authenticated
            )
            .httpBasic(Customizer.withDefaults()) // Enable basic authentication (username/password dialog)
            .build(); // Build and return the filter chain
    }
}
