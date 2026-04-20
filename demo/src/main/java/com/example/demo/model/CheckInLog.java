package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ENTITÀ che registra ogni accesso (CHECK-IN) a un evento.
 * 
 * Serve per tracciare chi è entrato e quando.
 */
@Entity
@Data
@Table(name = "check_in_logs")
@NoArgsConstructor
@AllArgsConstructor
public class CheckInLog {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * QR code del biglietto utilizzato per l'accesso.
     */
    private String qrCode;

    /**
     * Data e ora del check-in.
     */
    private LocalDateTime checkInTime;
}