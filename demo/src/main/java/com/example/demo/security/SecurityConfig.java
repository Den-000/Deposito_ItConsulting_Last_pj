package com.example.demo.security;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configurazione principale Spring Security + JWT
 *
 * Qui si definiscono:
 * - chi può accedere agli endpoint
 * - gestione sessioni (stateless)
 * - inserimento filtro JWT personalizzato
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // filtro personalizzato JWT che intercetta richieste HTTP
    private final JwtAuthFilter filter;

    // Dependency Injection del filtro
    public SecurityConfig(JwtAuthFilter filter) {
        this.filter = filter;
    }

    /**
     * Configura la catena di sicurezza HTTP
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http

            // disabilita protezione CSRF (non necessaria per API REST JWT)
            .csrf(csrf -> csrf.disable())

            // definisce che NON useremo sessioni (JWT è stateless)
            .sessionManagement(s ->
                s.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // regole di autorizzazione
            .authorizeHttpRequests(auth -> auth

                // endpoint pubblici (accessibili senza login)
                // il controllo avverrà a livello di URL, quindi qualsiasi richiesta a questi percorsi è permessa
                // IMPORTANTE:
                // il JwtAuthFilter viene comunque eseguito su ogni richiesta,
                // ma NON blocca automaticamente le richieste:
                // si limita a leggere il token e, se valido, impostare l'utente nel SecurityContext
                //
                // La decisione finale sull'accesso viene fatta da:
                // .anyRequest().authenticated()
                .requestMatchers(
                    "/**/*.html",
                    "/**/*.js",
                    "/**/*.css",
                    "/favicon.ico",
                    "/auth/**"
                ).permitAll()

                // qualsiasi altra richiesta richiede autenticazione
                .anyRequest().authenticated()
            );

        // inserisce il filtro JWT PRIMA del filtro di autenticazione standard
        http.addFilterBefore(filter,
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}