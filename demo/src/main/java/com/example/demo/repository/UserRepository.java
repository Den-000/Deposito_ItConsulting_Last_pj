package com.example.demo.repository;

import com.example.demo.model.MyUser;
import com.example.demo.model.Role;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<MyUser, Long> {
    Optional<MyUser> findByUsername(String username);

    Optional<MyUser> findByEmail(String email);

    long countByRole(Role role);
}