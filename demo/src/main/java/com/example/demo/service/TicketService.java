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
import com.example.demo.dto.NotificationDTO;
import com.example.demo.dto.TicketDTO;
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
import com.example.demo.repository.TicketRepository;
import com.example.demo.repository.TicketTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final TicketRepository ticketRepository;
    private final MyUserRepository userRepository;
    private final EmailService emailService;
    private final QRCodeService qrCodeService;

    @Transactional
    public TicketResponse bookTicket(TicketRequest request) {
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new RuntimeException("Evento non trovato"));

        if (event.getStatus() != EventStatus.ACTIVE) {
            throw new RuntimeException("Evento non attivo");
        }

        long soldTickets = ticketRepository.countByEventId(event.getId());
        if (soldTickets >= event.getMaxSeats()) {
            throw new RuntimeException("Evento sold out");
        }

        TicketType type = ticketTypeRepository.findById(request.getTicketTypeId())
                .orElseThrow(() -> new RuntimeException("Ticket type non trovato"));

        if (type.getAvailableSeats() <= 0) {
            throw new RuntimeException("Biglietti esauriti per questo tipo");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        MyUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Ticket ticket = new Ticket();
        ticket.setEvent(event);
        ticket.setTicketType(type);
        ticket.setQrCode(qrCodeService.generate());
        ticket.setUser(user);
        ticket.setFirstName(request.getFirstName());
        ticket.setLastName(request.getLastName());
        ticket.setEmail(request.getEmail());
        ticket.setValid(true);
        ticket.setCheckedIn(false);
        ticket.setPurchaseDate(LocalDateTime.now());

        type.setAvailableSeats(type.getAvailableSeats() - 1);
        event.setBookedSeats(event.getBookedSeats() + 1);

        // ---------------- PAYMENT  (CASCADE) ----------------
        Payment payment = new Payment();
        payment.setAmount(type.getPrice());
        payment.setStatus(PaymentStatus.PAID);
        payment.setMethod("CARD");
        payment.setPaymentDate(LocalDateTime.now());

        payment.setTicket(ticket);
        ticket.setPayment(payment);

        Ticket saved = ticketRepository.save(ticket);

        if (saved.getPayment() != null && saved.getPayment().getStatus() == PaymentStatus.PAID) {
            emailService.sendTicketPaymentSummary(saved, saved.getPayment());
        }

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
        String email = auth.getName();

        System.out.println("AUTH NAME: " + email);

        MyUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        return ticketRepository.findByUserOrderByEvent_DateDesc(user).stream()
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
                    r.setFirstName(ticket.getFirstName());
                    r.setLastName(ticket.getLastName());
                    return r;
                })
                .toList();
    }

    // Metodo per admin: ottenere i biglietti di un utente specifico
    public List<TicketDTO> getUserTickets(Long userId) {
        List<Ticket> tickets = ticketRepository.findByUser_Id(userId);

        return tickets.stream()
                .map(t -> new TicketDTO(
                        t.getId(),
                        t.getEvent().getName(),
                        t.getEmail(),
                        t.getPurchaseDate(),
                        t.isValid(),
                        t.isCheckedIn(),
                        t.getTicketType().getName(),
                        t.getTicketType().getPrice(),

                        t.getPayment() != null ? t.getPayment().getAmount() : null,
                        t.getPayment() != null ? t.getPayment().getMethod() : null,
                        t.getPayment() != null ? t.getPayment().getStatus().name() : null
                ))
                .toList();
    }

    // Metodo per admin: ottenere i dettagli di un biglietto specifico
    public TicketDTO getTicketById(Long id) {
        Ticket t = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket non trovato"));

        return new TicketDTO(
                t.getId(),
                t.getEvent().getName(),
                t.getEmail(),
                t.getPurchaseDate(),
                t.isValid(),
                t.isCheckedIn(),
                t.getTicketType().getName(),
                t.getTicketType().getPrice(),

                t.getPayment() != null ? t.getPayment().getAmount() : null,
                t.getPayment() != null ? t.getPayment().getMethod() : null,
                t.getPayment() != null ? t.getPayment().getStatus().name() : null
        );
    }

    public long getSoldTickets(Long eventId) {
        return ticketRepository.countByEventId(eventId);
    }

    public List<NotificationDTO> getAllNotifications() {
    return ticketRepository.findAll().stream()
            .map(ticket -> new NotificationDTO(
                    ticket.getUser().getEmail(),
                    ticket.getEvent().getName(),
                    ticket.getEvent().getDate(),
                    ticket.isReminderSent()
            ))
            .toList();
}

}