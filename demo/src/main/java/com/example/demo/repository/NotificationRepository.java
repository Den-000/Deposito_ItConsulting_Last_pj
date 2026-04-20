package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Notification;

/**
 * REPOSITORY per la gestione delle NOTIFICHE inviate agli utenti.
 */
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Recupera tutte le notifiche inviate a una specifica email.
     */
    List<Notification> findByEmail(String email);

    /**
     * Recupera notifiche inviate dopo una certa data.
     */
    List<Notification> findBySentAtAfter(LocalDateTime date);

    /**
     * Recupera notifiche fallite (non inviate correttamente).
     */
    List<Notification> findBySuccessFalse();
}