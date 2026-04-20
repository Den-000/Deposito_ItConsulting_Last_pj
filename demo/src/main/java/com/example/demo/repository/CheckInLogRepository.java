package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.CheckInLog;

/**
 * REPOSITORY per la gestione dei LOG di check-in.
 * 
 * Questo livello permette di accedere al database senza scrivere SQL,
 * usando le query automatiche di Spring Data JPA.
 */
public interface CheckInLogRepository extends JpaRepository<CheckInLog, Long> {

    /**
     * Recupera tutti i check-in associati a uno specifico QR code.
     * 
     * Utile per:
     * - verificare ingressi
     * - audit degli accessi
     */
    List<CheckInLog> findByQrCode(String qrCode);
}