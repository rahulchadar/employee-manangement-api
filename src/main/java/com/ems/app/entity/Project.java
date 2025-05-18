package com.ems.app.entity;

import java.time.LocalDate;
import java.util.List;

import com.ems.app.util.IDGenerator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "project") // Maps this entity to the "project" table in the database
public class Project {

	@Id // Primary key for the project table
	private String projectId;
	
	@Column(nullable = false) // Project name is required (non-null)
	private String projectName;
	
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Date format for serialization
	private LocalDate startDate = LocalDate.now(); // Defaults to current date
	
	@Column(nullable = false)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private LocalDate endDate; // Required project end date
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude if null during JSON serialization
	private LocalDate updatedDeadline; // Optional field for updated deadline

	@ManyToOne // Many projects can be associated with one client
	@JoinColumn(name="clientId", referencedColumnName = "clientId") // Foreign key column linking to Client entity
	@JsonIgnore // Exclude from JSON response to avoid recursion or sensitive data exposure
	private Client client;

	@JsonIgnore // Prevent recursive serialization of employees in response
	@OneToMany(mappedBy = "project", cascade = CascadeType.ALL) // One project can have many employees
	private List<Employee> employees;

	@PrePersist // Lifecycle hook that runs before saving the entity
	public void assignProjectId() {
	    if (this.projectId == null || this.projectId.isEmpty()) {
	        this.projectId = IDGenerator.generateProjectId(); // Automatically generates a project ID
	    }
	}

	// Getters and Setters

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public LocalDate getUpdatedDeadline() {
		return updatedDeadline;
	}

	public void setUpdatedDeadline(LocalDate updatedDeadline) {
		this.updatedDeadline = updatedDeadline;
	}
}
