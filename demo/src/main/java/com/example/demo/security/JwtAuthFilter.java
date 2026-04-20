package com.example.demo.security;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * FILTRO JWT:
 *
 * Questo filtro intercetta OGNI richiesta HTTP in ingresso
 * e gestisce l'autenticazione basata su token JWT.
 *
 * Flusso:
 * 1. Legge header Authorization
 * 2. Estrae il token JWT
 * 3. Valida il token
 * 4. Recupera l'utente
 * 5. Imposta autenticazione nel SecurityContext
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userService;

    public JwtAuthFilter(JwtService jwtService,
                         CustomUserDetailsService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    /**
     * Esclude endpoint pubblici (es. /auth/**)
     * 
     * Questi endpoint non richiedono autenticazione.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth/");
    }

    /**
     * Metodo principale del filtro.
     * Viene eseguito per ogni richiesta HTTP.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {

            // 1. Lettura header Authorization
            String header = request.getHeader("Authorization");

            // 2. Controllo presenza Bearer token
            if (header != null && header.startsWith("Bearer ")) {

                // 3. Estrazione token
                String token = header.substring(7);

                // 4. Estrazione username dal token
                String username = jwtService.extractUsername(token);

                // 5. Autenticazione solo se non già presente
                if (username != null &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {

                    // 6. Caricamento utente dal database
                    UserDetails user = userService.loadUserByUsername(username);

                    // 7. Creazione oggetto autenticazione Spring Security
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    user.getAuthorities()
                            );

                    // 8. Aggiunta dettagli richiesta HTTP
                    auth.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // 9. Salvataggio nel contesto di sicurezza
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

        } catch (Exception e) {
            // In caso di errore JWT, si resetta l'autenticazione
            System.out.println("JWT ERROR: " + e.getMessage());
            SecurityContextHolder.clearContext();
        }

        // Continuazione della catena dei filtri
        chain.doFilter(request, response);
    }
}