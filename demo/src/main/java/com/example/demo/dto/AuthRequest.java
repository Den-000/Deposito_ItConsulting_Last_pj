package com.example.demo.dto;

/**
 * DTO = Data Transfer Object
 *
 * Serve per trasportare dati dal frontend al backend.
 */
public class AuthRequest {

    private String username; // campo username
    private String password; // campo password

    // getter necessari per Spring (deserializzazione JSON → Java)

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}