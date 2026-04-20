package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ che rappresenta una notifica inviata all'utente.
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Email destinatario della notifica
     */
    private String email;

    /**
     * Tipo di notifica (conferma, promemoria, ecc.)
     */
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    /**
     * Data e ora di invio
     */
    private LocalDateTime sentAt;

    /**
     * Indica se l'invio è andato a buon fine
     */
    private boolean success;
}