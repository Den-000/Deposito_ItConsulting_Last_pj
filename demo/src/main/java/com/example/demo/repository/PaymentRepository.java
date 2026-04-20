package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Payment;
import com.example.demo.model.Ticket;

/**
 * REPOSITORY per la gestione dei PAGAMENTI.
 */
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    /**
     * Trova il pagamento associato a un ticket specifico.
     */
    Optional<Payment> findByTicket(Ticket ticket);
}