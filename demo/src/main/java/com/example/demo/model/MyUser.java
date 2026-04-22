package com.example.demo.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String name;
    private String surname;

    @Column(unique = true)
    private String email;
    
    private String phone;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnoreProperties({"user", "payment"})
    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;
}