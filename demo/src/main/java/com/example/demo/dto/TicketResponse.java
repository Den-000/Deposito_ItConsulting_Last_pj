package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO di RISPOSTA dopo l'acquisto di un biglietto.
 * 
 * Contiene informazioni utili per l'utente finale.
 */
@Data
@Builder
public class TicketResponse {

    /**
     * Codice QR generato per il biglietto.
     */
    private String qrCode;

    /**
     * Nome dell'evento.
     */
    private String eventName;

    /**
     * Tipo di biglietto (VIP, STANDARD).
     */
    private String ticketType;

    /**
     * Prezzo pagato.
     */
    private double price;

    /**
     * Indica se il biglietto è valido.
     */
    private boolean valid;
}