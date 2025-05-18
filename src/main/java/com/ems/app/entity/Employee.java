package com.ems.app.entity; // Declares the package

import java.time.LocalDate;

import com.ems.app.util.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "employee") // Maps this entity to the "employee" table
public class Employee {

	@Id // Specifies the primary key
	private String employeeId;
	
	@Column(nullable = false) // Field "name" cannot be null in the database
	private String name;
	
	@Column(nullable = false) // Field "email" cannot be null in the database
	private  String email;
	
	@Column(nullable = false) // Field "phone" cannot be null in the database
	private String phone;
	
	@Column(nullable = false) // Field "department" cannot be null in the database
	private String department;
	
	@Column(nullable = false) // Field "joiningDate" cannot be null in the database
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Specifies date format for JSON serialization
	private LocalDate joiningDate = LocalDate.now(); // Defaults to current date
	
	@ManyToOne // Many employees can belong to one project
    @JoinColumn(name = "projectId", referencedColumnName = "projectId") // Defines the foreign key relationship
	@JsonIgnore // Prevents recursive serialization or hiding project info in API response
	private Project project;
	
	@JsonIgnore // Prevents user details from appearing in API responses
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true) // One-to-one with User, with cascade and orphan removal
    @JoinColumn(name = "user_id", referencedColumnName = "id") // Foreign key to User table
	private User user;

	@PrePersist // Method called before saving the entity
	public void assignEmployeeId() {
	    if (this.employeeId == null || this.employeeId.isEmpty()) {
	        this.employeeId = IDGenerator.generateEmployeeId(); // Auto-generates employee ID
	    }
	}

	// Getters and setters

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public LocalDate getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(LocalDate joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	 
}
