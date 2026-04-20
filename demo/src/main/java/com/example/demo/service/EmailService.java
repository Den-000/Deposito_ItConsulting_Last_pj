package com.example.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * SERVIZIO per l'invio di email.
 */
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Invia il biglietto via email all'utente.
     */
    public void sendTicket(String to, String qr) {

        // Creazione messaggio email semplice (testo semplice)
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("Il tuo biglietto");
        msg.setText("QR Code: " + qr);

        // Invio email
        mailSender.send(msg);
    }
}