package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    private String imageUrl;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private int maxSeats;

    private int bookedSeats;

    @ManyToOne
    private Location location;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<TicketType> ticketTypes;

    @JsonIgnoreProperties({"event"})
    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;
}