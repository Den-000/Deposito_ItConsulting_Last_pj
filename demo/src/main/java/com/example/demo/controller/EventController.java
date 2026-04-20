package com.example.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Event;
import com.example.demo.repository.EventRepository;

import lombok.RequiredArgsConstructor;

/**
 * CONTROLLER REST per la gestione degli eventi.
 * 
 * Questo livello espone API HTTP che permettono:
 * - Creazione d'un evento
 * - Lettura degli eventi con paginazione
 * - Ricerca eventi per nome
 * 
 * Il controller NON contiene logica di business:
 * si limita a ricevere richieste e delegare al Repository.
 */
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    /**
     * Repository JPA che gestisce le operazioni sul database.
     * Iniettato automaticamente da Spring (Dependency Injection).
     */
    private final EventRepository repo;

    /**
     * CREA un nuovo evento.
     * Metodo HTTP: POST /events
     * 
     * @param event oggetto evento inviato dal client (JSON)
     * @return evento salvato nel database
     */
    @PostMapping
    public Event create(@RequestBody Event event) {
        return repo.save(event);
    }

    /**
     * RESTITUISCE tutti gli eventi con PAGINAZIONE.
     * Metodo HTTP: GET /events/all
     * 
     * Pageable permette di gestire:
     * - numero pagina
     * - dimensione pagina
     * - ordinamento
     */
    @GetMapping("/all")
    public Page<Event> getAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    /**
     * RICERCA eventi per nome (anche parziale e case-insensitive).
     * Metodo HTTP: GET /events/search?name=xxx
     * 
     * @param name testo da cercare nel nome evento
     * @return lista di eventi che contengono il testo
     */
    @GetMapping("/search")
    public List<Event> getByName(@RequestParam String name) {
        return repo.findByNameContainingIgnoreCase(name);
    }
}