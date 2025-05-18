package com.ems.app.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.ems.app.dto.APIResponse;
import com.ems.app.entity.Client;
import com.ems.app.entity.Project;
import com.ems.app.repository.ClientRepository;
import com.ems.app.repository.ProjectRepository;


@Service // Marks this class as a Spring service component to be managed by the container
public class ProjectService {

    @Autowired // Injects an instance of ProjectRepository into this service
    ProjectRepository projectRepository;

    @Autowired // Injects an instance of ClientRepository into this service
    ClientRepository clientRepository;

    // Method to save a project for a given client
    public APIResponse<Project> saveProject(String clientId, Project project){
        APIResponse<Project> projectResponse = new APIResponse<Project>(); // Creating a response object

        Optional<Client> client = clientRepository.findById(clientId.toUpperCase()); // Finding the client by ID (uppercase for consistency)

        if(!client.isPresent()) { // If client is not found
            projectResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status code to 404
            projectResponse.setMessage("Client Not Found with id: " + clientId); // Set message
            return projectResponse; // Return early with error response
        }

        if(project.getEndDate().isBefore(LocalDate.now())) { // If project end date is before today
            projectResponse.setStatusCode(HttpStatus.CONFLICT.value()); // Set status code to 409 (conflict)
            projectResponse.setMessage("End Date must be after today!!"); // Set error message
            return projectResponse; // Return early
        }

        project.setClient(client.get()); // Set the client in the project object
        projectRepository.save(project); // Save the project to the database

        projectResponse.setStatusCode(HttpStatus.CREATED.value()); // Set status code to 201 (created)
        projectResponse.setMessage("Project Added Successfully to clientId: " + client.get().getClientId()); // Set success message
        projectResponse.setData(project); // Set the saved project as response data

        return projectResponse; // Return the final response
    }

    // Method to retrieve a project by its ID
    public APIResponse<Project> getProjectById(String projectId){
        Optional<Project> project = projectRepository.findById(projectId.toUpperCase()); // Look for project by ID (uppercase)
        APIResponse<Project> projectResponse = new APIResponse<Project>(); // Create response object

        if(project.isPresent()) { // If project is found
            projectResponse.setStatusCode(HttpStatus.OK.value()); // Set status to 200
            projectResponse.setMessage("Project Found!!!!"); // Set success message
            projectResponse.setData(project.get()); // Set found project
            return projectResponse; // Return success response
        }

        projectResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status to 404
        projectResponse.setMessage("Project Not found with id: " + projectId); // Set error message
        return projectResponse; // Return error response
    }

    // Method to delete a project by ID
    public APIResponse<Project> deleteProject(String projectId){
        Optional<Project> project = projectRepository.findById(projectId.toUpperCase()); // Look for project by ID
        APIResponse<Project> projectResponse = new APIResponse<Project>(); // Create response object

        if(project.isPresent()) { // If project is found
            projectRepository.delete(project.get()); // Delete the project
            projectResponse.setStatusCode(HttpStatus.ACCEPTED.value()); // Set status to 202
            projectResponse.setMessage("Project Deleted Successfully!!!!"); // Set success message
            projectResponse.setData(project.get()); // Include deleted project in response
            return projectResponse; // Return success response
        }

        projectResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status to 404
        projectResponse.setMessage("Project Not found with ID: " + projectId); // Set error message
        return projectResponse; // Return error response
    }

    // Method to retrieve all projects
    public APIResponse<List<Project>> getAllProjects(){
        List<Project> projects = projectRepository.findAll(); // Get all projects from DB
        APIResponse<List<Project>> projectResponse = new APIResponse<>(); // Create response object

        if(projects.isEmpty()) { // If no projects found
            projectResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status to 404
            projectResponse.setMessage("No Projects Available: "); // Set message
            return projectResponse; // Return error response
        }

        projectResponse.setStatusCode(HttpStatus.OK.value()); // Set status to 200
        projectResponse.setMessage(projects.size() + " Projects Available"); // Set message with count
        projectResponse.setData(projects); // Add project list to response
        return projectResponse; // Return success response
    }

    // Method to get all projects associated with a client
    public APIResponse<List<Project>> getProjectByClient(String clientId){
        APIResponse<List<Project>> projectResponse = new APIResponse<>(); // Create response object
        Optional<Client> clientOp = clientRepository.findById(clientId.toUpperCase()); // Get client by ID

        if(clientOp.isEmpty()) { // If client is not found
            projectResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status to 404
            projectResponse.setMessage("Client Not Found with id: " + clientId); // Set error message
            return projectResponse; // Return error response
        }

        List<Project> projects = projectRepository.findByClient_clientId(clientId); // Get projects of the client

        if(projects.isEmpty()) { // If no projects found
            projectResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status to 404
            projectResponse.setMessage("No Projects Available for client: " + clientId); // Set message
            return projectResponse; // Return error response
        }

        projectResponse.setStatusCode(HttpStatus.OK.value()); // Set status to 200
        projectResponse.setMessage(projects.size() + " Projects Available for client: " + clientId); // Set message with count
        projectResponse.setData(projects); // Add list of projects to response
        return projectResponse; // Return success response
    }

    // Method to get client details by project ID
    public APIResponse<Client> getClientByProject(String projectId){
        APIResponse<Client> response = new APIResponse<Client>(); // Create response object
        Optional<Project> project = projectRepository.findById(projectId.toUpperCase()); // Get project by ID

        if(project.isEmpty()) { // If project not found
            response.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status to 404
            response.setMessage("Project Not Found with id: " + projectId); // Set error message
            return response; // Return error response
        }

        response.setStatusCode(HttpStatus.OK.value()); // Set status to 200
        response.setMessage("Client details for project: " + projectId); // Set message
        response.setData(project.get().getClient()); // Set client data from the project
        return response; // Return success response
    }

    // Method to update project details
    public APIResponse<Project> updateProject(String projectId, Project project){
        APIResponse<Project> projectResponse = new APIResponse<Project>(); // Create response object

        Optional<Project> projectOp = projectRepository.findById(projectId); // Get project by ID

        if(projectOp.isEmpty()) { // If project not found
            projectResponse.setStatusCode(HttpStatus.NOT_FOUND.value()); // Set status to 404
            projectResponse.setMessage("Project not found for: " + projectId); // Set message
            return projectResponse; // Return error response
        }

        Project existingProject = projectOp.get(); // Get the existing project

        if(project.getUpdatedDeadline() != null && project.getUpdatedDeadline().isBefore(LocalDate.now())){ // If invalid updated deadline
            projectResponse.setStatusCode(HttpStatus.CONFLICT.value()); // Set status to 409
            projectResponse.setMessage("Updated deadline must be after today!!"); // Set error message
            return projectResponse; // Return error response
        }

        if(project.getProjectName() != null) { // If new project name is provided
            existingProject.setProjectName(project.getProjectName()); // Update project name
        }

        if(project.getUpdatedDeadline() != null) { // If new deadline is provided
            existingProject.setUpdatedDeadline(project.getUpdatedDeadline()); // Update deadline
        }

        projectRepository.save(existingProject); // Save updated project to DB

        projectResponse.setStatusCode(HttpStatus.OK.value()); // Set status to 200
        projectResponse.setMessage("Project Updated Successfully for: " + projectId); // Set success message
        projectResponse.setData(existingProject); // Add updated project to response

        return projectResponse; // Return final response
    }

}

