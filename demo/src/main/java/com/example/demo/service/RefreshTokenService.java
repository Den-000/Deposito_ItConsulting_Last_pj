package com.example.demo.service;

import java.sql.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.model.RefreshToken;
import com.example.demo.repository.RefreshTokenRepository;

/**
 * Gestisce i refresh token:
 *
 * Access token (JWT) → breve durata
 * Refresh token → lunga durata (7 giorni)
 *
 * Serve per rigenerare JWT senza rifare login.
 */
@Service
public class RefreshTokenService {

    // accesso al database refresh token
    private final RefreshTokenRepository repo;

    public RefreshTokenService(RefreshTokenRepository repo) {
        this.repo = repo;
    }

    /**
     * CREA un nuovo refresh token
     */
    public String createToken(String username) {

        RefreshToken rt = new RefreshToken();

        // associa token all'utente
        rt.setUsername(username);

        // genera stringa casuale unica
        rt.setToken(UUID.randomUUID().toString());

        // scadenza: 7 giorni
        rt.setExpiryDate(new Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000));

        // salva nel database
        repo.save(rt);

        // restituisce token al client
        return rt.getToken();
    }

    /**
     * VALIDAZIONE refresh token
     */
    public RefreshToken validate(String token) {

        // cerca token nel database
        RefreshToken rt = repo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        // verifica scadenza
        if (rt.getExpiryDate().before(new java.util.Date())) {

            // elimina token scaduto
            repo.delete(rt);

            throw new RuntimeException("Expired refresh token");
        }

        return rt;
    }

    /**
     * ELIMINA refresh token (logout)
     */
    public void delete(String token) {
        repo.deleteByToken(token);
    }
}