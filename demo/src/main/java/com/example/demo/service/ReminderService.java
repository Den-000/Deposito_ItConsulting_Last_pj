package com.example.demo.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.model.Event;
import com.example.demo.model.Ticket;
import com.example.demo.repository.EventRepository;
import com.example.demo.repository.TicketRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final EmailService emailService;

    @Scheduled(cron = "0 0 9 * * *")
    public void sendEventReminders() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        LocalDateTime start = tomorrow.atStartOfDay();
        LocalDateTime end = tomorrow.atTime(LocalTime.MAX);

        List<Event> events = eventRepository.findByDateBetween(start, end);

        for (Event event : events) {
            List<Ticket> tickets = ticketRepository.findByEvent(event);

            for (Ticket ticket : tickets) {
                if (ticket.isValid()) {
                    emailService.sendReminderEmail(ticket);
                }
            }
        }

        System.out.println("Reminder eventi inviati per il giorno: " + tomorrow);
    }
}