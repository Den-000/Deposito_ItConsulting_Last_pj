package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.model.MyUser;
import com.example.demo.model.Role;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshService;
    private final MyUserRepository repo;
    private final PasswordEncoder encoder;

    public AuthService(AuthenticationManager authManager,
                       JwtService jwtService,
                       RefreshTokenService refreshService,
                       MyUserRepository repo,
                       PasswordEncoder encoder) {

        this.authManager = authManager;
        this.jwtService = jwtService;
        this.refreshService = refreshService;
        this.repo = repo;
        this.encoder = encoder;
    }

   /**
     * LOGIN:
     * 1. verifica credenziali
     * 2. genera JWT
     * 3. genera refresh token
     */
   public AuthResponse login(AuthRequest req) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(),
                        req.getPassword()
                )
        );

        MyUser user = repo.findByUsername(req.getUsername()).orElseThrow();

        String access = jwtService.generateToken(user.getUsername(), (user.getRole().name()));
        String refresh = refreshService.createToken(user.getUsername());

        return new AuthResponse(access, refresh);
    }

    /**
     * REGISTRAZIONE
     */
    public void register(AuthRequest req) {

        MyUser u = new MyUser();
        u.setUsername(req.getUsername());

        // password sempre hashata
        u.setPassword(encoder.encode(req.getPassword()));

        u.setRole(Role.USER);

        repo.save(u);
    }

    /**
     * LOGOUT
     */
    public void logout(String refreshToken) {
        refreshService.delete(refreshToken);
    }
}