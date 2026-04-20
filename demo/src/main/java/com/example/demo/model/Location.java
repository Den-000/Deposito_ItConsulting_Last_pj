package com.example.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ENTITÀ che rappresenta il luogo fisico di un evento.
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Nome del luogo (es. Teatro, Stadio)
     */
    private String name;

    /**
     * Indirizzo completo
     */
    private String address;

    /**
     * Città della location
     */
    private String city;
}