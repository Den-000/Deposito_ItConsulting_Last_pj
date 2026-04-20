package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Event;
import com.example.demo.model.TicketType;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    List<TicketType> findByEvent(Event event);
}