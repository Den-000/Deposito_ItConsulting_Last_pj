package com.example.demo.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.MyUser;

/**
 * Repository JPA per accesso al database utenti.
 *
 * Spring genera automaticamente l'implementazione.
 */
public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    /**
     * Query derivata:
     * SELECT * FROM users WHERE username = ?
     * 
     * Utilizzata per login e autenticazione.
     */
    Optional<MyUser> findByUsername(String username);

    /**
     * Verifica se esiste già un username nel sistema.
     * Utile per registrazione utenti.
     */
    boolean existsByUsername(String username);
}