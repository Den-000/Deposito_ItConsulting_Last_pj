package com.example.demo.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITÀ che rappresenta un pagamento effettuato.
 */
@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * Importo pagato
     */
    private double amount;

    /**
     * Stato del pagamento
     */
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    /**
     * Metodo di pagamento (es. CARD, PAYPAL)
     */
    private String method;

    /**
     * Data del pagamento
     */
    private LocalDateTime paymentDate;

    /**
     * Ticket associato al pagamento
     */
    @OneToOne
    private Ticket ticket;
}