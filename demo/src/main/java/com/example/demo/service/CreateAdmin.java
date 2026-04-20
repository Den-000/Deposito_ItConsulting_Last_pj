package com.example.demo.service;

import com.example.demo.model.MyUser;
import com.example.demo.model.Role;
import com.example.demo.repository.MyUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

/**
 * Classe che crea un utente admin automaticamente all'avvio.
 * Serve per testare subito il login senza usare DB manualmente.
 */
@Component
public class CreateAdmin {

    private final MyUserRepository repo;
    private final PasswordEncoder encoder;

    public CreateAdmin(MyUserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    /**
     * Viene eseguito automaticamente quando Spring avvia l'app
     */
    @PostConstruct
    public void init() {

        if (repo.findByUsername("admin").isEmpty()) {

            MyUser admin = new MyUser();

            admin.setUsername("admin");

            // PASSWORD DEVE ESSERE HASHATA
            admin.setPassword(encoder.encode("1234"));

            admin.setRole(Role.ADMIN);

            repo.save(admin);

            System.out.println("Admin creato: admin / 1234");
        }
    }
}