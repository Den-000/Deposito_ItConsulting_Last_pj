package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ che rappresenta un TIPO DI BIGLIETTO.
 * 
 * Esempi: VIP, STANDARD, EARLY ACCESS
 */
@Entity
@Data
@Table(name = "ticket_types")
@AllArgsConstructor
@NoArgsConstructor
public class TicketType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome del tipo di biglietto
     */
    private String name;

    /**
     * Prezzo del biglietto
     */
    private double price;

    /**
     * Numero totale di biglietti disponibili per questo tipo
     */
    private int totalSeats;

    /**
     * Posti ancora disponibili
     */
    private int availableSeats;

    /**
     * Evento a cui appartiene questo tipo di biglietto
     */
    @ManyToOne
    @JsonBackReference
    private Event event;
}