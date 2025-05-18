package com.ems.app.entity; // Package declaration

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity // Marks this class as a JPA entity
@Table(name = "contactPerson") // Maps this entity to the "contactPerson" table in the database
public class ContactPerson {

	@Id // Specifies the primary key of the entity
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generates ID using the identity strategy
	private Long id;
	
	@Column(nullable = false) // Makes "name" field not nullable in the database
	private String name;
	
	@Column(nullable = false) // Makes "email" field not nullable in the database
	private String email;
	
	@Column(nullable = false) // Makes "designation" field not nullable in the database
	private String designation;
	
	@ManyToOne // Many contact persons can belong to one client
	@JoinColumn(name = "clientId", referencedColumnName = "clientId") // Specifies foreign key column
	@JsonBackReference // Prevents infinite recursion during JSON serialization (paired with @JsonManagedReference)
	private Client client;
	
	@JsonIgnore // Excludes this field from JSON serialization
	@OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true) // One-to-one relationship with User entity
    @JoinColumn(name = "user_id", referencedColumnName = "id") // Specifies foreign key column linking to User
	private User user;

	// Getter and setter for id
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	// Getter and setter for name
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	// Getter and setter for email
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	// Getter and setter for designation
	public String getDesignation() {
		return designation;
	}
	public void setDesignation(String designation) {
		this.designation = designation;
	}

	// Getter and setter for client
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}

	// Getter and setter for user
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
