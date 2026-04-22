package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TicketDTO {

    private Long id;
    private String eventName;
    private String email;
    private LocalDateTime purchaseDate;
    private boolean valid;
    private boolean checkedIn;
    private String ticketTypeName;
    private Double ticketPrice;
    private Double paymentAmount;
    private String paymentMethod;
    private String paymentStatus;
}