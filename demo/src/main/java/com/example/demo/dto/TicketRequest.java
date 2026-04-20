package com.example.demo.dto;

import lombok.Data;

/**
 * DTO per la RICHIESTA DI ACQUISTO BIGLIETTO.
 * 
 * Contiene solo i dati necessari per creare un ticket.
 */
@Data
public class TicketRequest {

    /**
     * ID dell'evento selezionato.
     */
    private Long eventId;

    /**
     * Tipo di biglietto scelto (VIP, STANDARD, ecc.)
     */
    private Long ticketTypeId;

    /**
     * Email dell'acquirente.
     */
    private String email;
}