package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

import java.util.Date;

/**
 * Classe che gestisce creazione e lettura dei JWT.
 *
 * JWT = JSON Web Token:
 * - serve per autenticazione stateless
 * - contiene informazioni (username, ruolo)
 * - firmato digitalmente per evitare modifiche
 */
@Service
public class JwtService {

    // Chiave segreta usata per firmare e verificare i token JWT
    // !! In un'app reale, questa chiave dovrebbe essere più lunga e conservata in modo sicuro!!
    private static final String SECRET = "my-secret-key-my-secret-key-123456";

    // Conversione della stringa segreta in chiave crittografica compatibile con HMAC
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /**
     * GENERAZIONE TOKEN JWT
     *
     * @param username → identità utente
     * @param role → ruolo (USER, ADMIN, ecc.)
     * @return stringa JWT firmata
     */
    public String generateToken(String username, String role) {

        return Jwts.builder()

                // "subject" = identificatore principale del token (qui username)
                .setSubject(username)

                // aggiunge informazioni personalizzate dentro il token (payload)
                .claim("role", role)

                // data di creazione token
                .setIssuedAt(new Date())

                // data di scadenza (qui: 1 ora = 3600000 ms)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))

                // firma digitale del token con chiave segreta
                .signWith(key)

                // genera stringa JWT finale
                .compact();
    }

    /**
     * Estrae username dal token JWT
     *
     * @param token JWT ricevuto dal client
     * @return username contenuto nel token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()

                // imposta chiave per verificare firma
                .setSigningKey(key)

                // costruisce parser JWT
                .build()

                // decodifica token e verifica firma
                .parseClaimsJws(token)

                // recupera payload (claims)
                .getBody()

                // prende subject (username)
                .getSubject();
    }

    /**
     * Estrae ruolo dal token JWT
     */
    public String extractRole(String token) {
        return Jwts.parserBuilder()

                // imposta chiave per verificare firma
                .setSigningKey(key)

                // costruisce parser JWT
                .build()

                // decodifica token e verifica firma
                .parseClaimsJws(token)

                // recupera payload (claims)
                .getBody()

                // recupera campo custom "role"
                .get("role", String.class);
    }
}