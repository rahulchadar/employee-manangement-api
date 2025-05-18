package com.ems.app.util;

import com.ems.app.repository.ClientRepository;
import com.ems.app.repository.ProjectRepository;
import com.ems.app.repository.EmployeeRepository;
import org.springframework.stereotype.Component;

@Component
public class IDGenerator {

    private static ClientRepository clientRepository;
    private static ProjectRepository projectRepository;
    private static EmployeeRepository employeeRepository;

 
    public IDGenerator(ClientRepository clientRepo, ProjectRepository projectRepo, EmployeeRepository employeeRepo) {
        clientRepository = clientRepo;
        projectRepository = projectRepo;
        employeeRepository = employeeRepo;
    }

    public static String generateClientId() {
        long count = clientRepository.count() + 1;
        return String.format("CLIENT-%03d", count);
    }

    public static String generateProjectId() {
        long count = projectRepository.count() + 1;
        return String.format("PROJECT-%03d", count);
    }

    public static String generateEmployeeId() {
        long count = employeeRepository.count() + 1;
        return String.format("JTC-%03d", count);
    }
}
