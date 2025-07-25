package com.ems.app.dto; // Package declaration for DTO classes

import org.springframework.stereotype.Component; // Marks the class as a Spring-managed component

@Component // Indicates that this class is a Spring Bean (can be autowired if needed)
public class APIResponse<T> { // Generic wrapper class for API responses

	private int statusCode; // HTTP status code to indicate response status
	private String message; // Response message providing additional context
	private T Data; // Generic data field to hold response payload (e.g., entity or list)
	
	private String date;

	public int getStatusCode() { // Getter for statusCode
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) { // Setter for statusCode
		this.statusCode = statusCode;
	}
	
	public String getMessage() { // Getter for message
		return message;
	}
	
	public void setMessage(String message) { // Setter for message
		this.message = message;
	}
	
	public Object getData() { // Getter for data (returns Object, although field is generic T)
		return Data;
	}
	
	public void setData(T data) { // Setter for data
		Data = data;
	}
	
}
