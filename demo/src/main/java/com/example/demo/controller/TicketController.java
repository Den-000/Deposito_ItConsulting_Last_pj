package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.CheckInRequest;
import com.example.demo.dto.MyTicketResponse;
import com.example.demo.dto.TicketRequest;
import com.example.demo.dto.TicketResponse;
import com.example.demo.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public TicketResponse book(@RequestBody TicketRequest request) {
        return ticketService.bookTicket(request);
    }

    @PostMapping("/check-in")
    public void checkIn(@RequestBody CheckInRequest request) {
        ticketService.checkIn(request);
    }

    @GetMapping("/my")
    public List<MyTicketResponse> getMyTickets() {
        return ticketService.getMyTickets();
    }
}