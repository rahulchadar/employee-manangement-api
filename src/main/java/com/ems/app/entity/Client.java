package com.ems.app.entity; // Package declaration

import java.time.LocalDate;
import java.util.List;

import com.ems.app.util.IDGenerator; // Utility for generating client ID
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "client") // Maps to "client" table in the database
public class Client {

	@Id // Marks clientId as primary key
	private String clientId;
	
	@Column(nullable = false) // Column cannot be null
	private String companyName;
	
	@Column(nullable = false) // Column cannot be null
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy") // Format date in JSON as dd-MM-yyyy
	private LocalDate relationshipDate = LocalDate.now(); // Default to current date
	
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@JsonManagedReference // Used for handling bidirectional relationships with ContactPerson
	List<ContactPerson> contactPersons;
	
	@OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore // Prevents serialization to avoid infinite recursion
	List<Project> projects;

	public Client() {} // Default constructor

	@PrePersist // Called before saving the entity
	public void assignClientId() {
	    if (this.clientId == null || this.clientId.isEmpty()) {
	        this.clientId = IDGenerator.generateClientId(); // Generate client ID if not set
	    }
	}

	// Getter and setter for clientId
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	// Getter and setter for companyName
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	// Getter and setter for relationshipDate
	public LocalDate getRelationshipDate() {
		return relationshipDate;
	}
	public void setRelationshipDate(LocalDate relationshipDate) {
		this.relationshipDate = relationshipDate;
	}

	// Getter and setter for contactPersons
	public List<ContactPerson> getContactPersons() {
		return contactPersons;
	}
	public void setContactPersons(List<ContactPerson> contactPersons) {
		this.contactPersons = contactPersons;
	}

	// Getter and setter for projects
	public List<Project> getProjects() {
		return projects;
	}
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}
	
}
