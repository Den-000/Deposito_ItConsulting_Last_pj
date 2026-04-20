package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Location;

/**
 * REPOSITORY per le LOCATION degli eventi.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {

    /**
     * Cerca una location tramite nome.
     * Ritorna Optional perché potrebbe non esistere.
     */
    Optional<Location> findByName(String name);

    /**
     * Verifica se esiste già una location con stesso nome e città.
     * Utile per evitare duplicati nel database.
     */
    boolean existsByNameAndCity(String name, String city);
}