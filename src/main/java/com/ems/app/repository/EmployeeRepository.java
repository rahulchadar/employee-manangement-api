package com.ems.app.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.app.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    // Find an employee by their email
    Optional<Employee> findByEmail(String email);
    
    // Find all employees associated with a specific project ID
    List<Employee> findByProject_projectId(String projectId);
}
