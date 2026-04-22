package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyTicketResponse {

    private Long id;
    private String eventName;
    private LocalDateTime eventDate;
    private String eventLocation;
    private String ticketType;
    private BigDecimal price;
    private String qrCode;
    private boolean valid;
    private boolean checkedIn;
    private LocalDateTime purchaseDate;
    private String email;
    private String firstName;
    private String lastName;
}