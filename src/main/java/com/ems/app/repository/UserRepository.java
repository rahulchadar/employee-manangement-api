package com.ems.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ems.app.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Retrieves a User entity based on the provided email
    Optional<User> findByEmail(String email);
}
