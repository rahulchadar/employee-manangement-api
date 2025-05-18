package com.ems.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.app.entity.Client;

@Repository  // Marks this interface as a Spring Data repository
public interface ClientRepository extends JpaRepository<Client, String> {
    // Inherits default CRUD methods for Client entity (with clientId as primary key of type String)
}
