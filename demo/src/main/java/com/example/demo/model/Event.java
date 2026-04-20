package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ che rappresenta un EVENTO.
 * 
 * Un evento può avere:
 * - più tipi di biglietti
 * - una location
 */
@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Nome dell'evento (es. "Concerto", "Conferenza")
     */
    private String name;

    /**
     * Descrizione dettagliata dell'evento
     */
    private String description;

    /**
     * Data e ora dell'evento
     */
    private LocalDateTime date;

    /**
     * Stato dell'evento:
     * ACTIVE, CANCELLED, FINISHED
     */
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    /**
     * Numero massimo di posti disponibili
     */
    private int maxSeats;

    /**
     * Posti già prenotati
     */
    private int bookedSeats;

    /**
     * Location dell'evento (relazione molti-a-uno)
     */
    @ManyToOne
    private Location location;

    /**
     * Tipologie di biglietti associate all'evento
     * (VIP, STANDARD, ecc.)
     * 
     * JsonIgnore evita loop infiniti nella serializzazione JSON
     */
    @JsonIgnore
    @OneToMany(mappedBy = "event")
    private List<TicketType> ticketTypes;
}