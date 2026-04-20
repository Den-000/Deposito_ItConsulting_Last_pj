package com.example.demo.dto;

/**
 * Risposta del login
 *
 * Contiene:
 * - access token (JWT)
 * - refresh token
 */
public class AuthResponse {

    private String access;
    private String refresh;

    public AuthResponse(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    public String getAccess() {
        return access;
    }

    public String getRefresh() {
        return refresh;
    }
}