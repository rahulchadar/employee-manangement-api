// Define the package where this class belongs
package com.ems.app.service;

// Import Java utility classes for working with lists and optionals
import java.util.List;
import java.util.Optional;

// Import Spring framework classes for dependency injection, HTTP status codes, and service annotation
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Import custom DTO and Entity classes
import com.ems.app.dto.APIResponse;
import com.ems.app.dto.SetPasswordRequest;
import com.ems.app.entity.Employee;
import com.ems.app.entity.Project;
import com.ems.app.entity.RoleType;
import com.ems.app.entity.User;
import com.ems.app.repository.EmployeeRepository;
import com.ems.app.repository.ProjectRepository;

// Import Jakarta Transactional annotation to manage transactions
import jakarta.transaction.Transactional;

// Marks this class as a Spring-managed service component
@Service
// Ensures that methods in this class run within a database transaction
@Transactional
public class EmployeeService {

    // Inject the EmployeeRepository dependency using Spring's @Autowired annotation
    @Autowired
    EmployeeRepository employeeRepository;
    
    @Autowired
    ProjectRepository projectRepository;
    
    @Autowired
    PasswordEncoder passwordEncoder;

    // Method to save a new employee
    public APIResponse<Employee> saveEmployee(Employee employee){
        // Create a new response object for sending back API results
        APIResponse<Employee> employeeResponse = new APIResponse<>();

        // Check if the email already exists in the database
        if(employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            // If it exists, return a conflict response
            employeeResponse.setStatusCode(HttpStatus.CONFLICT.value()); // 409
            employeeResponse.setMessage("Email is already exist");
            employeeResponse.setData(null);
            return employeeResponse; // Return early to avoid saving
        }

        // If email is unique, save the employee to the database
        employeeRepository.save(employee);

        // Set success response
        employeeResponse.setStatusCode(HttpStatus.OK.value()); // 201
        employeeResponse.setMessage("Employee Saved Successfully!!!!");
        employeeResponse.setData(employee);

        // Return the response with saved data
        return employeeResponse;
    }

    // Method to retrieve an employee using employeeId
    public APIResponse<Employee> getEmployeeById(String employeeId){
        // Try to find employee by ID
        Optional<Employee> employee = employeeRepository.findById(employeeId.toUpperCase());

        // Prepare response object
        APIResponse<Employee> employeeResponse = new APIResponse<>();

        // Check if employee was found
        if(employee.isPresent()) {
            employeeResponse.setStatusCode(HttpStatus.OK.value()); // 200
            employeeResponse.setMessage("Employee Found!!!!");
            employeeResponse.setData(employee.get()); // Set found employee in response
        } else {
            // If not found, return 404
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Employee Not found with id: " + employeeId);
        }
        return employeeResponse; // Return the API response
    }
    
    
    // Method to retrieve an employee project using employeeId
    public APIResponse<Project> getProjectByEmployeeId(String employeeId){
        // Try to find employee by ID
        Optional<Employee> employee = employeeRepository.findById(employeeId.toUpperCase());

        // Prepare response object
        APIResponse<Project> response = new APIResponse<>();

        // Check if employee was found
        if(employee.isEmpty()) {
        	response.setStatusCode(HttpStatus.NOT_FOUND.value()); // 200
        	response.setMessage("Employee Not found with id: " + employeeId);
        	return response;
        }
        
        Project project = employee.get().getProject();
        
        if(project == null) {
        	response.setStatusCode(HttpStatus.NOT_FOUND.value()); // 200
        	response.setMessage("Employee On bench: " + employeeId);
        	return response;
        }
        
        response.setStatusCode(HttpStatus.FOUND.value()); // 200
        response.setMessage("Project Details for: " + employeeId);	
        response.setData(project);
       
        return response; // Return the API response
    }
    
    
 // Method to retrieve an employee project using employeeId
    public APIResponse<Project> getProjectByEmployeeEmail(String email){
        // Try to find employee by ID
        Optional<Employee> employee = employeeRepository.findByEmail(email);

        // Prepare response object
        APIResponse<Project> response = new APIResponse<>();

        // Check if employee was found
        if(employee.isEmpty()) {
        	response.setStatusCode(HttpStatus.NOT_FOUND.value()); // 200
        	response.setMessage("Employee Not found with email: " + email);
        	return response;
        }
        
        Project project = employee.get().getProject();
        
        if(project == null) {
        	response.setStatusCode(HttpStatus.NOT_FOUND.value()); // 200
        	response.setMessage("Employee On bench");
        	return response;
        }
        
        response.setStatusCode(HttpStatus.FOUND.value()); // 200
        response.setMessage("Project Details for:");	
        response.setData(project);
       
        return response; // Return the API response
    }
    
    
    

    // Method to retrieve an employee using email
    public APIResponse<Employee> getEmployeeByEmail(String email){
        // Find employee by email
        Optional<Employee> employee = employeeRepository.findByEmail(email);

        // Prepare response object
        APIResponse<Employee> employeeResponse = new APIResponse<>();

        // Check if employee was found
        if(employee.isPresent()) {
            employeeResponse.setStatusCode(HttpStatus.OK.value()); // 200
            employeeResponse.setMessage("Employee Found!!!!");
            employeeResponse.setData(employee.get());
        } else {
            // If not found, return 404
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Employee Not found with Email: " + email);
        }
        return employeeResponse; // Return the API response
    }

    // Method to delete an employee by ID
    public APIResponse<Employee> deleteEmployee(String employeeId){
        // Try to find employee by ID
        Optional<Employee> employee = employeeRepository.findById(employeeId.toUpperCase());

        // Prepare response object
        APIResponse<Employee> employeeResponse = new APIResponse<>();

        // If employee exists, delete it
        if(employee.isPresent()) {
            employeeRepository.delete(employee.get()); // Delete employee from DB
            employeeResponse.setStatusCode(HttpStatus.ACCEPTED.value()); // 202
            employeeResponse.setMessage("Employee Deleted Successfully!!!!");
            employeeResponse.setData(employee.get()); // Include deleted employee in response
        } else {
            // If not found, return 404
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Employee Not found with ID: " + employeeId);
        }
        return employeeResponse; // Return the API response
    }

    // Method to get all employees
    public APIResponse<List<Employee>> getAllEmployees(){
        // Retrieve all employees from the repository
        List<Employee> employees = employeeRepository.findAll();

        // Prepare response object
        APIResponse<List<Employee>> employeeResponse = new APIResponse<>();

        // If no employees are found
        if(employees.isEmpty()) {
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("No Employees Available: ");
        } else {
            // If employees are found, return them
            employeeResponse.setStatusCode(HttpStatus.OK.value()); // 200
            employeeResponse.setMessage("Employees Available");
            employeeResponse.setData(employees);
        }
        return employeeResponse; // Return the API response
    }

    // Method to update an existing employee
    public APIResponse<Employee> updateEmployee(String employeeId, Employee employee){
        // Try to find the existing employee by ID
        Employee existingEmployee = employeeRepository.findById(employeeId.toUpperCase()).orElse(null);

        // Prepare response object
        APIResponse<Employee> employeeResponse = new APIResponse<>();

        // If the employee is found
        if(existingEmployee != null) {
            // Update name if provided in request
            if(employee.getName() != null)
                existingEmployee.setName(employee.getName());

            // Update department if provided
            if(employee.getDepartment() != null)
                existingEmployee.setDepartment(employee.getDepartment());

            // Update phone if provided
            if(employee.getPhone() != null)
                existingEmployee.setPhone(employee.getPhone());

            // Save updated employee to database
            employeeRepository.save(existingEmployee);

            // Return success response
            employeeResponse.setStatusCode(HttpStatus.OK.value()); // 200
            employeeResponse.setMessage("Employees Updated");
            employeeResponse.setData(existingEmployee);
        } else {
            // If employee doesn't exist
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Employee Not found with ID: " + employeeId);
        }
        return employeeResponse; // Return the API response
    }
    
    
 // Method to assign a project to an employee using their IDs
    public APIResponse<Employee> assignProject(String employeeId, String projectId) {
        
        // Create a response object to return the result
        APIResponse<Employee> employeeResponse = new APIResponse<Employee>();
        
        // Try to find the employee by ID (convert to uppercase to match stored format)
        Optional<Employee> employeeOp = employeeRepository.findById(employeeId.toUpperCase());

        // If employee is not found, return 404 response
        if (employeeOp.isEmpty()) {
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Employee Not Found with Id: " + employeeId);
            return employeeResponse;
        }

        // Try to find the project by ID (convert to uppercase to match stored format)
        Optional<Project> project = projectRepository.findById(projectId.toUpperCase());

        // If project is not found, return 404 response
        if (project.isEmpty()) {
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Project Not Found with Id: " + projectId);
            return employeeResponse;
        }

        // Get the employee entity from the Optional
        Employee employee = employeeOp.get();

        // Check if the employee is already assigned to a project
        if (employee.getProject() != null) {
            employeeResponse.setStatusCode(HttpStatus.CONFLICT.value()); // 409
            employeeResponse.setMessage("Employee is already assigned to a project and cannot be reassigned");
            return employeeResponse;
        }

        // Assign the found project to the employee
        employee.setProject(project.get());

        // Save the updated employee back to the database
        employeeRepository.save(employee);

        // Prepare and return a success response
        employeeResponse.setStatusCode(HttpStatus.OK.value()); // 200
        employeeResponse.setMessage("Project assigned to employee successfully");
        employeeResponse.setData(employee); // Return the updated employee

        return employeeResponse; // Final response with status, message, and data
    }

    public APIResponse<Employee> releaseEmployeeFromProject(String employeeId){
        
        // Create a response object to hold the API response details
        APIResponse<Employee> employeeResponse = new APIResponse<Employee>();
        
        // Fetch the employee from the database by ID (converted to uppercase for consistency)
        Optional<Employee> employeeOp = employeeRepository.findById(employeeId.toUpperCase());
        
        // If employee not found, return 404 Not Found response
        if(employeeOp.isEmpty()) {
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Employee Not Found with Id: " + employeeId);
            return employeeResponse;
        }
        
        // Get the actual employee object from the Optional
        Employee employee = employeeOp.get();
        
        // If the employee is already on bench (i.e., not assigned to any project), return conflict response
        if(employee.getProject() == null) {
            employeeResponse.setStatusCode(HttpStatus.CONFLICT.value()); // 409
            employeeResponse.setMessage("Employee is already on bench");
            return employeeResponse;
        }
        
        // Remove the project assignment from the employee (putting them on bench)
        employee.setProject(null);
        
        // Save the updated employee back to the database
        employeeRepository.save(employee);
        
        // Prepare and return a success response
        employeeResponse.setStatusCode(HttpStatus.OK.value()); // 200
        employeeResponse.setMessage("Employee released from project successfully!");
        employeeResponse.setData(employee); // Return the updated employee
        
        return employeeResponse;
    }

    // Method to get employee by projects
    public APIResponse<List<Employee>> getEmployeesByProject(String projectId){

        // Prepare response object
        APIResponse<List<Employee>> employeeResponse = new APIResponse<>();

        // Fetch client by ID from the database (converting to uppercase for consistency)
        Optional<Project> projectOp = projectRepository.findById(projectId.toUpperCase());

        // If the client is not found
        if(projectOp.isEmpty()) {
        	employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set HTTP status code 404
        	employeeResponse.setMessage("Project Not Found with id: " + projectId); // Error message
            return employeeResponse; // Exit early if client is not found
        }

        // Retrieve all projects associated with the given client ID
        List<Employee> employees = employeeRepository.findByProject_projectId(projectId);

        // If no projects are found for the client
        if(employees.isEmpty()) {
        	employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set HTTP status code 404
        	employeeResponse.setMessage("No Employee on-boarded on project: " + projectId); // Informational message

            return employeeResponse; // Return the response indicating no projects found
        }

        // If projects are found, prepare a success response
        employeeResponse.setStatusCode(HttpStatus.OK.value()); // Set HTTP status code 200
        employeeResponse.setMessage(employees.size() + " Employees on-boarded on project: " + projectId); // Message with count
        employeeResponse.setData(employees); // Attach the list of projects to the response

        return employeeResponse; // Return the complete API response
    }
    
    
    public APIResponse<Employee> setPassword(SetPasswordRequest passwordRequest){
        // Create a response object to hold the result
        APIResponse<Employee> employeeResponse = new APIResponse<Employee>();
        
        // Try to fetch the employee by ID (converted to uppercase)
        Optional<Employee> employeeOp = employeeRepository.findById(passwordRequest.getId().toUpperCase());
        
        // If employee is not found, return a 404 response
        if(employeeOp.isEmpty()) {
            employeeResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // 404
            employeeResponse.setMessage("Employee Not found with id: " + passwordRequest.getId());
            return employeeResponse;
        }
        
        // Get the employee entity from the optional
        Employee employee = employeeOp.get();
        
        // Retrieve the associated User object if exists
        User user = employee.getUser();
        
        // If no user is associated, create a new User and set the email
        if(user == null) {
            user = new User();
            user.setEmail(employee.getEmail());
         // Set role and encoded password for the user
            user.setRole(RoleType.EMPLOYEE);
        }
        
        
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        
        // Associate the user with the employee
        employee.setUser(user);
        
        // Save the employee (cascades user save due to CascadeType.PERSIST)
        employeeRepository.save(employee);
        
        // Build success response
        employeeResponse.setStatusCode(HttpStatus.OK.value()); // 200 OK
        employeeResponse.setMessage("Successfully, Password set for the Employee: " + passwordRequest.getId());
        employeeResponse.setData(employee);
        
        return employeeResponse;
    }

    
    
    
    
    
    
    
    
    
    
    

}
