package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CheckInRequest;
import com.example.demo.dto.MyTicketResponse;
import com.example.demo.dto.TicketRequest;
import com.example.demo.dto.TicketResponse;
import com.example.demo.model.*;
import com.example.demo.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final MyUserRepository userRepository;
    @SuppressWarnings("unused")
    private final EmailService emailService;
    private final QRCodeService qrCodeService;

    @Transactional
    public TicketResponse bookTicket(TicketRequest request) {

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new RuntimeException("Evento non attivo");
        }

        if (event.getBookedSeats() >= event.getMaxSeats()) {
            throw new RuntimeException("Evento sold out");
        }

        TicketType type = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new RuntimeException("Ticket type non trovato"));

        if (type.getAvailableSeats() <= 0) {
            throw new RuntimeException("Biglietti esauriti per questo tipo");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // ---------------- TICKET ----------------
        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setTicketType(type);
        ticket.setQrCode(qrCodeService.generate());
        ticket.setUser(user);
        ticket.setEmail(request.getEmail());
        ticket.setValid(true);
        ticket.setCheckedIn(false);
        ticket.setPurchaseDate(LocalDateTime.now());

        // aggiorna disponibilità
        event.setBookedSeats(event.getBookedSeats() + 1);
        type.setAvailableSeats(type.getAvailableSeats() - 1);

        // ---------------- PAYMENT  (CASCADE) ----------------
        Payment payment = new Payment();
        payment.setAmount(type.getPrice());
        payment.setStatus(PaymentStatus.PAID);
        payment.setMethod("CARD");
        payment.setPaymentDate(LocalDateTime.now());

        // relazione bidirezionale
        payment.setTicket(ticket);
        ticket.setPayment(payment);

        // ---------------- SALVATAGGIO ----------------
        // SOLO ticket (payment viene salvato via cascade)
        Ticket saved = ticketRepository.save(ticket);

        // email opzionale
        // emailService.sendTicket(request.getEmail(), saved.getQrCode());

        return TicketResponse.builder()
                .qrCode(saved.getQrCode())
                .eventName(saved.getEvent().getName())
                .ticketType(saved.getTicketType().getName())
                .price(saved.getTicketType().getPrice())
                .valid(true)
                .build();
    }

    public void checkIn(CheckInRequest request) {

        Ticket ticket = ticketRepository.findByQrCode(request.getQrCode())
                .orElseThrow(() -> new RuntimeException("Ticket non trovato"));

        if (!ticket.isValid()) {
            throw new RuntimeException("Ticket non valido");
        }

        if (ticket.isCheckedIn()) {
            throw new RuntimeException("Già usato");
        }

        ticket.setCheckedIn(true);
        ticketRepository.save(ticket);

        System.out.println("Check-in effettuato ticket " + ticket.getId());
    }

    public List<MyTicketResponse> getMyTickets() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return ticketRepository.findByUser(user).stream()
                .map(ticket -> {
                    MyTicketResponse r = new MyTicketResponse();
                    r.setId(ticket.getId());
                    r.setEventName(ticket.getEvent().getName());
                    r.setEventDate(ticket.getEvent().getDate());
                    r.setEventLocation(
                            ticket.getEvent().getLocation() != null
                                    ? ticket.getEvent().getLocation().getName()
                                    : null
                    );
                    r.setTicketType(ticket.getTicketType().getName());
                    BigDecimal b = new BigDecimal(ticket.getTicketType().getPrice());
                    r.setPrice(b);
                    r.setQrCode(ticket.getQrCode());
                    r.setValid(ticket.isValid());
                    r.setCheckedIn(ticket.isCheckedIn());
                    r.setPurchaseDate(ticket.getPurchaseDate());
                    r.setEmail(ticket.getEmail());
                    return r;
                })
                .toList();
    }
}