package com.ems.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.app.entity.ContactPerson;

// Repository interface for ContactPerson entity
public interface ContactPersonRepository extends JpaRepository<ContactPerson, Long> {

    // Custom method to find a contact person by their email
    Optional<ContactPerson> findByEmail(String email);
}
