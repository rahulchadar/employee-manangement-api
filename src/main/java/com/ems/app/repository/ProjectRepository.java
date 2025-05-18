package com.ems.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.app.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {

    // Find all projects that belong to a specific client using clientId
    List<Project> findByClient_clientId(String clientId);
}
