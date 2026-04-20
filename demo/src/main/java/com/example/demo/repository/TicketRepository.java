package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Event;
import com.example.demo.model.MyUser;
import com.example.demo.model.Ticket;

/**
 * REPOSITORY per la gestione dei BIGLIETTI.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Trova tutti i biglietti di un evento.
     */
    List<Ticket> findByEvent(Event event);

    /**
     * Trova tutti i biglietti di un utente.
     */
    List<Ticket> findByUser(MyUser user);

    /**
     * Trova un biglietto tramite QR code.
     */
    Optional<Ticket> findByQrCode(String qrCode);

    /**
     * Conta i biglietti totali di un evento.
     */
    long countByEventId(Long eventId);

    /**
     * Conta i biglietti già usati (check-in effettuato).
     */
    long countByEventIdAndCheckedInTrue(Long eventId);
}