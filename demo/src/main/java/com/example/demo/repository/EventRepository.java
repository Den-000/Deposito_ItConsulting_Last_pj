package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Event;
import com.example.demo.model.EventStatus;

/**
 * REPOSITORY per la gestione degli EVENTI.
 * 
 * Fornisce query automatiche basate sui nomi dei metodi.
 */
public interface EventRepository extends JpaRepository<Event, Long> {

    /**
     * Cerca eventi che contengono il testo nel nome (case-insensitive).
     */
    List<Event> findByNameContainingIgnoreCase(String name);

    /**
     * Restituisce eventi futuri (dopo una certa data).
     */
    List<Event> findByDateAfter(LocalDateTime date);

    /**
     * Restituisce eventi in un intervallo temporale.
     */
    List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);

    /**
     * Filtra eventi in base allo stato (ACTIVE, CANCELLED, FINISHED).
     */
    List<Event> findByStatus(EventStatus status);
}