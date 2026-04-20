package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.RefreshToken;

/**
 * Repository JPA per accesso al database refresh tokens.
 *
 * Spring genera automaticamente l'implementazione.
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Query derivata:
     * SELECT * FROM refresh_tokens WHERE token = ?
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Query derivata:
     * DELETE FROM refresh_tokens WHERE token = ?
     */
    void deleteByToken(String token);
}