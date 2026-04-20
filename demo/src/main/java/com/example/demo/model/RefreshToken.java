package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
import java.sql.Date;

/**
 * Tabella che memorizza i refresh token
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;      // valore del refresh token
    private String username;   // utente associato
    private Date expiryDate;   // scadenza token
}