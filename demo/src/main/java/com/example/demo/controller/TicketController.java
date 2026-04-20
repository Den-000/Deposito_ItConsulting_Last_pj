package com.example.demo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CheckInRequest;
import com.example.demo.dto.TicketRequest;
import com.example.demo.dto.TicketResponse;
import com.example.demo.service.TicketService;

import lombok.RequiredArgsConstructor;

/**
 * CONTROLLER per la gestione dei TICKET.
 * 
 * Espone API per:
 * - Acquisto biglietti
 * - Check-in tramite QR code
 * 
 * La logica vera è nel TicketService.
 */
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    /**
     * Service che contiene la logica di business dei ticket.
     */
    private final TicketService ticketService;

    /**
     * ACQUISTO BIGLIETTO
     * Metodo HTTP: POST /tickets
     * 
     * Riceve i dati della richiesta e restituisce un TicketResponse
     * contenente informazioni del biglietto generato.
     */
    @PostMapping
    public TicketResponse book(@RequestBody TicketRequest request) {
        return ticketService.bookTicket(request);
    }

    /**
     * CHECK-IN BIGLIETTO
     * Metodo HTTP: POST /tickets/check-in
     * 
     * Scansionando il QR code, il sistema valida il biglietto
     * e registra l'ingresso dell'utente.
     */
    @PostMapping("/check-in")
    public void checkIn(@RequestBody CheckInRequest request) {
        ticketService.checkIn(request);
    }
}