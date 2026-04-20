package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity che rappresenta la tabella USERS nel database.
 */
@Entity // indica entità JPA
@Table(name = "users") // nome tabella

@Data // Lombok: genera getter, setter, toString, ecc.
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {

    @Id // chiave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto increment
    private Long id;

    private String username; // nome utente
    private String email;
    private String password; // password (DEVE essere hashata)
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN, USER
}