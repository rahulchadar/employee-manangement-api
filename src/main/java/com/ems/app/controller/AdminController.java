// Define the package where this controller class resides
package com.ems.app.controller;

// Import Spring annotation to mark this class as a REST controller
import org.springframework.web.bind.annotation.RestController;

// Import custom response wrapper and entity classes
import com.ems.app.dto.APIResponse;
import com.ems.app.dto.SetPasswordRequest;
import com.ems.app.entity.Client;
import com.ems.app.entity.ContactPerson;
import com.ems.app.entity.Employee;
import com.ems.app.entity.Project;
import com.ems.app.service.ClientService;
// Import the service layer that handles business logic
import com.ems.app.service.EmployeeService;
import com.ems.app.service.ProjectService;

// Import necessary Java utilities
import java.util.List;

// Import Spring annotations for dependency injection and request handling
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



// Mark this class as a REST controller, making it capable of handling HTTP requests
@RestController
// Prefix all endpoints in this controller with "/admin"
@RequestMapping("/admin")
public class AdminController {

    // Automatically inject the EmployeeService instance from the Spring context
    @Autowired
    private EmployeeService employeeService;
    
    // Automatically inject the ClientService instance from the Spring context
    @Autowired
    private ClientService clientService;

    // Automatically inject the ProjectService instance from the Spring context
    @Autowired
    private ProjectService projectService;
    
    /* ---------ADMIN CONTROLLS EMPLOYEE ---------------*/
    // Handle POST request to save a new employee
    @PostMapping("/saveEmployee")
    public APIResponse<Employee> saveEmployee(@RequestBody Employee employee) {
        // Pass the employee object to service method and return the API response
        return employeeService.saveEmployee(employee);
    }

    // Handle GET request to fetch an employee by ID
    @GetMapping("/getEmployeeById/{employeeId}")
    public APIResponse<Employee> getEmployeeById(@PathVariable String employeeId) {
        // Use the path variable to fetch the employee by ID
        return employeeService.getEmployeeById(employeeId);
    }

    // Handle GET request to fetch an employee by their email
    @GetMapping("/getEmployeeByEmail/{email}")
    public APIResponse<Employee> getEmployeeByEmail(@PathVariable String email) {
        // Use the path variable to fetch the employee by email
        return employeeService.getEmployeeByEmail(email);
    }

    // Handle DELETE request to remove an employee by ID
    @DeleteMapping("/deleteEmployee/{employeeId}")
    public APIResponse<Employee> deleteEmployee(@PathVariable String employeeId) {
        // Delete the employee and return the result
        return employeeService.deleteEmployee(employeeId);
    }

    // Handle GET request to retrieve all employees
    @GetMapping("/getAllEmployees")
    public APIResponse<List<Employee>> getAllEmployees() {
        // Fetch and return the list of all employees
        return employeeService.getAllEmployees();
    }

    // Handle PUT request to update an existing employee
    @PutMapping("/updateEmployee/{employeeId}")
    public APIResponse<Employee> updateEmployee(@PathVariable String employeeId, @RequestBody Employee employee) {
        // Pass employeeId and updated data to the service to update
        return employeeService.updateEmployee(employeeId, employee);
    }
    
    
    
    /* ---------ADMIN CONTROLLS CLIENTS ---------------*/
    
    // Handle POST request to save a new client
    @PostMapping("/saveClient")
    public APIResponse<Client> saveClient(@RequestBody Client client) {
        // Pass the client object to service method and return the API response
        return clientService.saveClient(client);
    }
    
    // Handle GET request to fetch an client by ID
    @GetMapping("/getClientById/{clientId}")
    public APIResponse<Client> getClientById(@PathVariable String clientId) {
        // Use the path variable to fetch the client by ID
        return clientService.getClientById(clientId);
    }
    
    // Handle DELETE request to remove an client by ID
    @DeleteMapping("/deleteClient/{clientId}")
    public APIResponse<Client> deleteClient(@PathVariable String clientId) {
        // Delete the client and return the result
        return clientService.deleteClient(clientId);
    }
    
    // Handle POST request to add contact person to client
    @PostMapping("/addContact/{clientId}")
    public APIResponse<ContactPerson> saveContactPerson(@PathVariable String clientId, @RequestBody ContactPerson contactPerson) {
        return clientService.addContact(clientId, contactPerson);
    }
    
    // Handle GET request to retrieve all clients
    @GetMapping("/getAllClients")
    public APIResponse<List<Client>> getAllClients() {
        // Fetch and return the list of all clients
        return clientService.getAllClients();
    }
    
    
    	/* ---------ADMIN CONTROLLS PROJECTS ---------------*/
    
    @PostMapping("/saveProject/{clientId}")
    public APIResponse<Project> saveProject(@PathVariable String clientId, @RequestBody Project project) {
        return projectService.saveProject(clientId, project);
    }
    
    @GetMapping("/getProjectById/{projectId}")
    public APIResponse<Project> getProject(@PathVariable String projectId) {
        return projectService.getProjectById(projectId);
    }
    
    // Handle DELETE request to remove an project by ID
    @DeleteMapping("/deleteProject/{projectId}")
    public APIResponse<Project> deleteProject(@PathVariable String projectId) {
        // Delete the project and return the result
        return projectService.deleteProject(projectId);
    }
    
    // Handle GET request to retrieve all projects
    @GetMapping("/getAllProjects")
    public APIResponse<List<Project>> getAllProjects() {
        // Fetch and return the list of all projects
        return projectService.getAllProjects();
    }
    
    @PutMapping("/updateProject/{projectId}")
    public APIResponse<Project> updateProject(@PathVariable String projectId, @RequestBody Project project) {
        return projectService.updateProject(projectId, project);
        		
    }
    
    
    
    /* ------ ADMIN OTHER CONTROLS---------*/
    
    
    
    // Handles HTTP PUT requests to assign a project to an employee using their IDs
    @PutMapping("/assignProject/employee/{employeeId}/project/{projectId}") 
    public APIResponse<Employee> assignProject(@PathVariable String employeeId,  
                                               @PathVariable String projectId) {
        // Calls the service layer to perform the logic of assigning the project to the employee
        return employeeService.assignProject(employeeId, projectId); 
    }

    // Handles HTTP PUT requests to unassign (remove) a project from an employee using their ID
    @PutMapping("/unassignProject/employee/{employeeId}") 
    public APIResponse<Employee> unassignProjectFromEmployee(@PathVariable String employeeId) {
        // Calls the service layer to perform the logic of unassigning the project from the employee
        return employeeService.releaseEmployeeFromProject(employeeId); 
    }

    // Handles HTTP GET requests to the specified URL pattern: /getProjectsByclient/{clientId}
    @GetMapping("/getProjectsByClient/{clientId}") 
    // Defines a controller method that returns a list of projects associated with a given clientId
    public APIResponse<List<Project>> getProjectsByClient(@PathVariable String clientId) { 
        // Calls the service method to fetch the list of projects for the specified clientId
        return projectService.getProjectByClient(clientId); 
    }
    
    // Handles HTTP GET requests to the specified URL pattern: /getProjectsByclient/{clientId}
    @GetMapping("/getEmployeesByProject/{projectId}") 
    // Defines a controller method that returns a list of projects associated with a given clientId
    public APIResponse<List<Employee>> getEmployeesByProject(@PathVariable String projectId) { 
        // Calls the service method to fetch the list of projects for the specified clientId
        return employeeService.getEmployeesByProject(projectId);
    }
    
    // Maps HTTP GET requests to get client of specific project method, with a path variable 'projectId'
    @GetMapping("/getClientByProject/{projectId}")  
    public APIResponse<Client> getClientByProject(@PathVariable String projectId) {  // Retrieves 'projectId' from the URL and passes it to the method
        return projectService.getClientByProject(projectId);  // Calls the service method to get the client by project ID and returns the response
    }
    
 // Maps HTTP GET requests to get client of specific project method, with a path variable 'projectId'
    @GetMapping("/getProjectByEmployee/{employeeId}")  
    public APIResponse<Project> getProejctByEmployee(@PathVariable String employeeId) {  // Retrieves 'projectId' from the URL and passes it to the method
        return employeeService.getProjectByEmployeeId(employeeId);  // Calls the service method to get the client by project ID and returns the response
    }
    
    
    
    
    /*-----------SET password for the EMPLOYEE----------*/
    
    @PutMapping("/setEmployeePassword") // Handles HTTP PUT requests to /employee/setEmployeePassword
    public APIResponse<Employee> setEmployeePassword(@RequestBody SetPasswordRequest setPasswordRequest) {
        
        return employeeService.setPassword(setPasswordRequest); // Calls service to set password for an employee
    }

    @PutMapping("/setClientPassword") // Handles HTTP PUT requests to /client/setClientPassword
    public APIResponse<Client> setClientPassword(@RequestBody SetPasswordRequest setPasswordRequest) {
        
        return clientService.setPassword(setPasswordRequest); // Calls service to set password for a client
    }


    
    

    
    
    
    
    

}
