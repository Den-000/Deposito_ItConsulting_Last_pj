package com.example.demo.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ che rappresenta un BIGLIETTO acquistato.
 */
@Entity
@Table(name = "tickets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Codice QR univoco del biglietto
     */
    private String qrCode;

    /**
     * Indica se il biglietto è valido
     */
    private boolean valid;

    /**
     * Indica se il biglietto è già stato usato (check-in effettuato)
     */
    private boolean checkedIn;

    /**
     * Data di acquisto del biglietto
     */
    private LocalDateTime purchaseDate;

    /**
     * Email associata all'acquisto
     */
    private String email;

    /**
     * Utente proprietario del ticket
     */
    @JsonIgnoreProperties({"tickets", "payments"})
    @ManyToOne
    private MyUser user;

    /**
     * Evento associato al ticket
     */
    @JsonIgnoreProperties({"tickets"})
    @ManyToOne
    private Event event;

    /**
     * Tipo di biglietto acquistato
     */
    @JsonIgnoreProperties({"tickets", "event"})
    @ManyToOne
    private TicketType ticketType;

    /**
     * Pagamento associato al ticket
     */
    @JsonIgnoreProperties({"ticket", "user"})
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Payment payment;
}