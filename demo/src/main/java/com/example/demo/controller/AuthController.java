package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.AuthService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per l'autenticazione.
 *
 * IMPORTANTE:
 * - Questo controller è pubblico
 * - NON richiede JWT
 * - Serve per login e registrazione
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    // Service che contiene la logica di business per quanto concerne l'autenticazione e la gestione degli utenti.
    private final AuthService service;

    // Dependency Injection tramite costruttore
    public AuthController(AuthService service) {
        this.service = service;
    }

    /**
     * LOGIN
     * Riceve username e password, restituisce token JWT + refresh token
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        return service.login(req);
    }

    /**
     * REGISTRAZIONE UTENTE
     */
    @PostMapping("/register")
    public void register(@RequestBody AuthRequest req) {
        service.register(req);
    }

    /**
     * LOGOUT
     * Invalida il refresh token
     */
    @PostMapping("/logout")
    public void logout(@RequestBody String refreshToken) {
        service.logout(refreshToken);
    }
}