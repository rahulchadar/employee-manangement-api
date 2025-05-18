package com.ems.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "users") // Maps the entity to the "users" table
public class User {
	
	@Id // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrementing ID
	private long id;
	
	@Column(nullable = false) // Email is mandatory
	private String email;
	
	@Column(nullable = false) // Password is mandatory
	private String password;
	
	@Enumerated(EnumType.STRING) // Stores the enum name (e.g., ADMIN, EMPLOYEE, CLIENT)
    @Column(nullable = false) // Role is mandatory
	private RoleType role;

	// Default constructor
	public User() {}

    // Parameterized constructor
    public User(String email, String password, RoleType role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

	// Getters and Setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RoleType getRole() {
		return role;
	}

	public void setRole(RoleType role) {
		this.role = role;
	}
}
