package com.ems.app.dto; // Package for Data Transfer Object (DTO) classes

public class SetPasswordRequest { // DTO class used to receive password update requests

	private String id; // Holds the ID of the user (could be employee ID, client ID, etc.)
	
	private String password; // Holds the new password to be set

	public String getId() { // Getter for id
		return id;
	}

	public void setId(String id) { // Setter for id
		this.id = id;
	}

	public String getPassword() { // Getter for password
		return password;
	}

	public void setPassword(String password) { // Setter for password
		this.password = password;
	}
	
}
