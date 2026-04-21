package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CheckInRequest;
import com.example.demo.dto.MyTicketResponse;
import com.example.demo.dto.TicketRequest;
import com.example.demo.dto.TicketResponse;
import com.example.demo.model.Event;
import com.example.demo.model.EventStatus;
import com.example.demo.model.MyUser;
import com.example.demo.model.Payment;
import com.example.demo.model.PaymentStatus;
import com.example.demo.model.Ticket;
import com.example.demo.model.TicketType;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.MyUserRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

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

        MyUser user = userRepository.findByUsername(request.getEmail())
                .orElse(null);

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setTicketType(type);
        ticket.setQrCode(qrCodeService.generate());
        ticket.setUser(user);
        ticket.setEmail(request.getEmail());
        ticket.setValid(true);
        ticket.setCheckedIn(false);
        ticket.setPurchaseDate(LocalDateTime.now());

        event.setBookedSeats(event.getBookedSeats() + 1);
        type.setAvailableSeats(type.getAvailableSeats() - 1);

        Payment payment = new Payment();
        payment.setAmount(type.getPrice());
        payment.setStatus(PaymentStatus.PAID);
        payment.setMethod("CARD");
        payment.setPaymentDate(LocalDateTime.now());

        payment.setTicket(ticket);
        ticket.setPayment(payment);

        ticketRepository.save(ticket);
        paymentRepository.save(payment);

        emailService.sendTicket(request.getEmail(), ticket.getQrCode());

        return TicketResponse.builder()
                .qrCode(ticket.getQrCode())
                .eventName(event.getName())
                .ticketType(type.getName())
                .price(type.getPrice())
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

        System.out.println("Check-in effettuato per ticket " + ticket.getId());
    }

    public List<MyTicketResponse> getMyTickets() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        MyUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return ticketRepository.findByUser(user).stream()
        .map(ticket -> MyTicketResponse.builder()
                .id(ticket.getId())
                .eventName(ticket.getEvent() != null ? ticket.getEvent().getName() : null)
                .eventDate(ticket.getEvent() != null ? ticket.getEvent().getDate() : null)
                .eventLocation(
                        ticket.getEvent() != null && ticket.getEvent().getLocation() != null
                                ? ticket.getEvent().getLocation().getName()
                                : null
                )
                .ticketType(ticket.getTicketType() != null ? ticket.getTicketType().getName() : null)
                .price(ticket.getTicketType() != null ? ticket.getTicketType().getPrice() : null)
                .qrCode(ticket.getQrCode())
                .valid(ticket.isValid())
                .checkedIn(ticket.isCheckedIn())
                .purchaseDate(ticket.getPurchaseDate())
                .email(ticket.getEmail())
                .build())
        .toList();
    }
}