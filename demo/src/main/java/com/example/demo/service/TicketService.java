package com.example.demo.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.demo.dto.*;
import com.example.demo.model.*;
import com.example.demo.repository.*;

import lombok.RequiredArgsConstructor;

/**
 * SERVIZIO PRINCIPALE per la gestione dei BIGLIETTI.
 *
 * Contiene tutta la logica di business:
 * - acquisto biglietti
 * - gestione posti
 * - pagamento simulato
 * - invio email
 * - check-in
 */
@Service
@RequiredArgsConstructor
public class TicketService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final PaymentRepository paymentRepository;
    private final MyUserRepository userRepository;
    private final EmailService emailService;
    private final QRCodeService qrCodeService;

    /**
     * ACQUISTO BIGLIETTO
     */
    public TicketResponse bookTicket(TicketRequest request) {

        // 1. RECUPERO EVENTO
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        // controllo stato evento
        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new RuntimeException("Evento non attivo");
        }

        // controllo posti disponibili evento
        if (event.getBookedSeats() >= event.getMaxSeats()) {
            throw new RuntimeException("Evento sold out");
        }

        // 2. RECUPERO TIPO BIGLIETTO
        TicketType type = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new RuntimeException("Ticket type non trovato"));

        // controllo disponibilità tipo biglietto
        if (type.getAvailableSeats() <= 0) {
            throw new RuntimeException("Biglietti esauriti per questo tipo");
        }

        // 3. RECUPERO UTENTE (opzionale)
        MyUser user = userRepository.findByUsername(request.getEmail())
                .orElse(null);

        // 4. GENERAZIONE QR CODE
        String qr = qrCodeService.generate();

        // 5. CREAZIONE TICKET
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setTicketType(type);
        ticket.setQrCode(qr);
        ticket.setUser(user);

        // ATTENZIONE: possibile bug logico (email = username)
        ticket.setEmail(user.getUsername());

        ticket.setValid(true);
        ticket.setCheckedIn(false);
        ticket.setPurchaseDate(LocalDateTime.now());

        // 6. AGGIORNAMENTO POSTI
        event.setBookedSeats(event.getBookedSeats() + 1);
        type.setAvailableSeats(type.getAvailableSeats() - 1);

        // 7. PAGAMENTO SIMULATO
        Payment payment = new Payment();
        payment.setAmount(type.getPrice());
        payment.setStatus(PaymentStatus.PAID);
        payment.setMethod("CARD");
        payment.setPaymentDate(LocalDateTime.now());

        payment.setTicket(ticket);
        ticket.setPayment(payment);

        // 8. SALVATAGGIO SU DB
        ticketRepository.save(ticket);
        paymentRepository.save(payment);

        // 9. INVIO EMAIL
        emailService.sendTicket(request.getEmail(), qr);

        // 10. RISPOSTA AL CLIENT
        return TicketResponse.builder()
                .qrCode(qr)
                .eventName(event.getName())
                .ticketType(type.getName())
                .price(type.getPrice())
                .valid(true)
                .build();
    }

    /**
     * CHECK-IN BIGLIETTO
     */
    public void checkIn(CheckInRequest request) {

        // ricerca ticket tramite QR code
        Ticket ticket = ticketRepository.findByQrCode(request.getQrCode())
                .orElseThrow(() -> new RuntimeException("Ticket non trovato"));

        // verifica validità
        if (!ticket.isValid()) {
            throw new RuntimeException("Ticket non valido");
        }

        // evita doppio ingresso
        if (ticket.isCheckedIn()) {
            throw new RuntimeException("Già usato");
        }

        // marca come usato
        ticket.setCheckedIn(true);

        // log semplice (debug)
        System.out.println("Check-in effettuato per ticket " + ticket.getId());
    }
}