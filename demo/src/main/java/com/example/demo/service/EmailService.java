package com.example.demo.service;

import java.time.format.DateTimeFormatter;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.demo.model.Payment;
import com.example.demo.model.Ticket;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Il tuo biglietto");

            String html = """
                    <div style="font-family: Arial, sans-serif; max-width: 700px; margin: 0 auto; background: #0f172a; color: #e5e7eb; border-radius: 16px; overflow: hidden; border: 1px solid #334155;">
                        <div style="background: linear-gradient(135deg, #4f46e5, #7c3aed); padding: 24px; text-align: center;">
                            <h1 style="margin: 0; font-size: 28px; color: white;">Pagamento completato</h1>
                            <p style="margin: 8px 0 0 0; color: #ede9fe;">Il tuo biglietto è stato confermato con successo</p>
                        </div>
                        <div style="padding: 28px;">
                            <p style="font-size: 16px; margin-bottom: 18px;">Di seguito trovi il codice del tuo biglietto:</p>
                            <div style="background: #1e293b; border: 1px solid #475569; border-radius: 12px; padding: 18px; text-align: center; margin-bottom: 22px;">
                                <p style="margin: 0 0 10px 0; color: #94a3b8; font-size: 14px;">QR Code</p>
                                <p style="margin: 0; font-size: 20px; font-weight: bold; color: #ffffff; word-break: break-word;">%s</p>
                            </div>
                            <p style="margin: 0; color: #cbd5e1;">Conserva questa email e mostra il codice al momento dell'accesso.</p>
                        </div>
                    </div>
                    """.formatted(qr);

            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Errore durante l'invio dell'email", e);
        }
    }

    public void sendTicketPaymentSummary(Ticket ticket, Payment payment) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(ticket.getEmail());
            helper.setSubject("Conferma acquisto biglietto - " + ticket.getEvent().getName());

            String eventDate = ticket.getEvent().getDate() != null
                    ? ticket.getEvent().getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "-";

            String paymentDate = payment.getPaymentDate() != null
                    ? payment.getPaymentDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                    : "-";

            String locationName = ticket.getEvent().getLocation() != null
                    ? ticket.getEvent().getLocation().getName()
                    : "-";

            String locationAddress = ticket.getEvent().getLocation() != null
                    ? ticket.getEvent().getLocation().getAddress()
                    : "-";

            String locationCity = ticket.getEvent().getLocation() != null
                    ? ticket.getEvent().getLocation().getCity()
                    : "-";

            String html = """
                    <div style="font-family: Arial, sans-serif; max-width: 760px; margin: 0 auto; background: #0f172a; color: #e5e7eb; border-radius: 16px; overflow: hidden; border: 1px solid #334155;">
                        <div style="background: linear-gradient(135deg, #4f46e5, #7c3aed); padding: 28px; text-align: center;">
                            <h1 style="margin: 0; font-size: 30px; color: white;">Acquisto confermato</h1>
                            <p style="margin: 10px 0 0 0; color: #ede9fe; font-size: 15px;">Il pagamento è stato completato con successo</p>
                        </div>

                        <div style="padding: 28px;">
                            <p style="font-size: 16px; margin-top: 0;">Ciao %s %s,</p>
                            <p style="font-size: 15px; color: #cbd5e1; margin-bottom: 24px;">
                                ecco il riepilogo del tuo acquisto. Conserva questa email e il codice del biglietto.
                            </p>

                            <div style="background: #1e293b; border: 1px solid #334155; border-radius: 14px; padding: 22px; margin-bottom: 20px;">
                                <h2 style="margin: 0 0 16px 0; font-size: 20px; color: #ffffff;">Dettagli evento</h2>
                                <p style="margin: 8px 0;"><strong>Evento:</strong> %s</p>
                                <p style="margin: 8px 0;"><strong>Data:</strong> %s</p>
                                <p style="margin: 8px 0;"><strong>Luogo:</strong> %s</p>
                                <p style="margin: 8px 0;"><strong>Indirizzo:</strong> %s, %s</p>
                                <p style="margin: 8px 0;"><strong>Tipo biglietto:</strong> %s</p>
                            </div>

                            <div style="background: #1e293b; border: 1px solid #334155; border-radius: 14px; padding: 22px; margin-bottom: 20px;">
                                <h2 style="margin: 0 0 16px 0; font-size: 20px; color: #ffffff;">Dettagli pagamento</h2>
                                <p style="margin: 8px 0;"><strong>Importo:</strong> € %s</p>
                                <p style="margin: 8px 0;"><strong>Metodo:</strong> %s</p>
                                <p style="margin: 8px 0;"><strong>Stato:</strong> %s</p>
                                <p style="margin: 8px 0;"><strong>Data pagamento:</strong> %s</p>
                            </div>

                            <div style="background: #111827; border: 1px dashed #6366f1; border-radius: 14px; padding: 22px; text-align: center;">
                                <p style="margin: 0 0 10px 0; color: #94a3b8; font-size: 14px;">QR Code del biglietto</p>
                                <p style="margin: 0; font-size: 20px; font-weight: bold; color: #ffffff; word-break: break-word;">%s</p>
                            </div>

                            <p style="margin-top: 24px; color: #cbd5e1; font-size: 14px;">
                                Presenta questo codice al check-in per accedere all'evento.
                            </p>
                        </div>
                    </div>
                    """.formatted(
                    valueOrDash(ticket.getFirstName()),
                    valueOrDash(ticket.getLastName()),
                    valueOrDash(ticket.getEvent().getName()),
                    eventDate,
                    locationName,
                    locationAddress,
                    locationCity,
                    valueOrDash(ticket.getTicketType().getName()),
                    String.valueOf(payment.getAmount()),
                    valueOrDash(payment.getMethod()),
                    payment.getStatus() != null ? payment.getStatus().name() : "-",
                    paymentDate,
                    valueOrDash(ticket.getQrCode())
            );

            helper.setText(html, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Errore durante l'invio dell'email", e);
        }
    }

    private String valueOrDash(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }
}