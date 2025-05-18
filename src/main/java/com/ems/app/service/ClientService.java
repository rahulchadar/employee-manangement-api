package com.ems.app.service; // Declares the package this class belongs to

import java.util.List; // Importing List interface for holding multiple elements
import java.util.Optional; // Importing Optional for safe null-checking

import org.springframework.beans.factory.annotation.Autowired; // Enables Spring's dependency injection
import org.springframework.http.HttpStatus; // For using HTTP status codes like 200, 404, etc.
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service; // Marks this class as a service component in Spring

import com.ems.app.dto.APIResponse; // Custom wrapper for API responses
import com.ems.app.dto.SetPasswordRequest;
import com.ems.app.entity.Client; // JPA entity representing a Client
import com.ems.app.entity.ContactPerson; // JPA entity representing a ContactPerson
import com.ems.app.entity.Project;
import com.ems.app.entity.RoleType;
import com.ems.app.entity.User;
import com.ems.app.repository.ClientRepository; // JPA repository for Client entity
import com.ems.app.repository.ContactPersonRepository; // JPA repository for ContactPerson entity

@Service // Tells Spring this class contains business logic and is a service bean
public class ClientService {

	@Autowired // Injects an instance of ClientRepository automatically
	ClientRepository clientRepository;
	
	@Autowired // Injects an instance of ContactPersonRepository automatically
	ContactPersonRepository contactPersonRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	// Saves a client and its contact persons if email(s) are not already in use
	public APIResponse<Client> saveClient(Client client) {

	    APIResponse<Client> clientResponse = new APIResponse<>(); // Create response wrapper

	    List<ContactPerson> contactPersons = client.getContactPersons(); // Get the list of contact persons from client

	    // Check if contact persons list is not null or empty
	    if (contactPersons != null && !contactPersons.isEmpty()) {

	        // Iterate through each contact person to check if their email already exists
	        for (ContactPerson contactPerson : contactPersons) {
	            if (contactPersonRepository.findByEmail(contactPerson.getEmail()).isPresent()) {
	                clientResponse.setStatusCode(HttpStatus.CONFLICT.value()); // 409 Conflict
	                clientResponse.setMessage("Email already exists: " + contactPerson.getEmail()); // Set error message
	                return clientResponse; // Return early if any email is already in use
	            }
	        }

	        // If all emails are unique, link each contact person to the client
	        for (ContactPerson contactPerson : contactPersons) {
	            contactPerson.setClient(client); // Set back-reference
	        }
	    }

	    // Save the client entity, which will also save contact persons due to cascading
	    clientRepository.save(client);

	    clientResponse.setStatusCode(HttpStatus.CREATED.value()); // 201 Created
	    clientResponse.setMessage("Client Saved Successfully!!!!"); // Set success message
	    clientResponse.setData(client); // Attach saved client data

	    return clientResponse; // Return the full response
	}
	
	// Retrieves a client by their ID
	public APIResponse<Client> getClientById(String clientId){
		Optional<Client> client = clientRepository.findById(clientId.toUpperCase()); // Search by ID
		
		APIResponse<Client> clientResponse = new APIResponse<Client>(); // Response wrapper

        if(client.isPresent()) {
        	clientResponse.setStatusCode(HttpStatus.OK.value()); // 200 OK
        	clientResponse.setMessage("Client Found!!!!"); // Success message
        	clientResponse.setData(client.get()); // Attach client object
        } else {
        	clientResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404 Not Found
        	clientResponse.setMessage("Client Not found with id: " + clientId); // Error message
        }
        return clientResponse; // Return API response
	}
	
	// Deletes a client by ID if it exists
    public APIResponse<Client> deleteClient(String clientId){
        Optional<Client> client = clientRepository.findById(clientId.toUpperCase()); // Find client by ID

        APIResponse<Client> clientResponse = new APIResponse<Client>(); // Response wrapper

        if(client.isPresent()) {
        	clientRepository.delete(client.get()); // Delete client
        	clientResponse.setStatusCode(HttpStatus.ACCEPTED.value()); // 202 Accepted
        	clientResponse.setMessage("Employee Deleted Successfully!!!!"); // Success message
        	clientResponse.setData(client.get()); // Return deleted client info
        } else {
        	clientResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404 Not Found
        	clientResponse.setMessage("client Not found with ID: " + clientId); // Error message
        }
        return clientResponse; // Return API response
    }

    // Adds a contact person to an existing client by client ID
    public APIResponse<ContactPerson> addContact(String clientId, ContactPerson contactPerson){
    	APIResponse<ContactPerson> contactResponse = new APIResponse<ContactPerson>(); // Create response

    	Optional<Client> client = clientRepository.findById(clientId.toUpperCase()); // Make ID case-insensitive
    	
    	if(!client.isPresent()) {
    		contactResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
    		contactResponse.setMessage("Client Not Found with id: " + clientId); // Error message
    	    return contactResponse; // Exit early if client is not found
    	}
    	
    	if(contactPersonRepository.findByEmail(contactPerson.getEmail()).isPresent()) {
    		contactResponse.setStatusCode(HttpStatus.CONFLICT.value()); // 409
    		contactResponse.setMessage("Email already exists: " + contactPerson.getEmail()); // Email already in use
            return contactResponse; // Exit early
    	}
    	
    	contactPerson.setClient(client.get()); // Set reference to parent client
		contactPersonRepository.save(contactPerson); // Save contact person

		contactResponse.setStatusCode(HttpStatus.CREATED.value()); // 201
		contactResponse.setMessage("Contact Saved Successfully to clientId: " + client.get().getClientId()); // Success message
		contactResponse.setData(contactPerson); // Return saved contact
    	
    	return contactResponse; // Final API response
    }
	
	// Retrieves all clients from the database
    public APIResponse<List<Client>> getAllClients(){
    	List<Client> clients = clientRepository.findAll(); // Fetch all clients

    	APIResponse<List<Client>> clientResponse = new APIResponse<List<Client>>(); // Response wrapper
    	
    	if(clients.isEmpty()) {
    		clientResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            clientResponse.setMessage("No Clients Available: "); // No data
            return clientResponse; // Return early
    	}
    	
    	clientResponse.setStatusCode(HttpStatus.OK.value()); // 200 OK
    	clientResponse.setMessage("Clients Available"); // Success message
    	clientResponse.setData(clients); // Attach list of clients
    	
    	return clientResponse; // Return final response
    }
    
    
 // Sets a common password for all contact persons under a specific client
    public APIResponse<Client> setPassword(SetPasswordRequest passwordRequest) {
        APIResponse<Client> clientResponse = new APIResponse<>();

        // Convert ID to uppercase and try to find the client
        Optional<Client> clientOp = clientRepository.findById(passwordRequest.getId().toUpperCase());

        if (clientOp.isEmpty()) {
            clientResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            clientResponse.setMessage("Client Not found with id: " + passwordRequest.getId());
            return clientResponse;
        }

        Client client = clientOp.get();

        // Iterate through all contact persons linked to the client
        for (ContactPerson contactPerson : client.getContactPersons()) {
            User user = contactPerson.getUser();

            // If user doesn't exist, create and link a new user
            if (user == null) {
                user = new User();
                user.setEmail(contactPerson.getEmail());
                user.setRole(RoleType.CLIENT);
            }

            user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
            // Set user back to contact person and save
            contactPerson.setUser(user);
            contactPersonRepository.save(contactPerson);
        }

        clientResponse.setStatusCode(HttpStatus.OK.value());
        clientResponse.setMessage("Successfully, Password set for the client: " + passwordRequest.getId());
        clientResponse.setData(client);

        return clientResponse;
    }

    // Fetches a client's full details using a contact person's email
    public APIResponse<Client> getClientByEmail(String email) {
        APIResponse<Client> clientResponse = new APIResponse<>();

        // Find the contact person and get the associated client
        Client client = contactPersonRepository.findByEmail(email).get().getClient();

        clientResponse.setStatusCode(HttpStatus.OK.value()); // 200 OK
        clientResponse.setMessage("Clients Details");
        clientResponse.setData(client);

        return clientResponse;
    }

    // Fetches all projects for a client using a contact person's email
    public APIResponse<List<Project>> getClientProjectsByEmail(String email) {
        APIResponse<List<Project>> response = new APIResponse<>();

        // Find the contact person and get the associated client
        Client client = contactPersonRepository.findByEmail(email).get().getClient();
        List<Project> projects = client.getProjects();

        // Handle empty project list
        if (projects.isEmpty()) {
            response.setStatusCode(HttpStatus.NOT_FOUND.value());
            response.setMessage("Projects are not available....");
            return response;
        }

        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("Projects Detail for Client: " + client.getClientId());
        response.setData(projects);

        return response;
    }
  

} // End of ClientService class
