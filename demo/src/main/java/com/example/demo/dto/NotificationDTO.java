package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationDTO {

    private String email;
    private String eventName;
    private LocalDateTime eventDate;
    private boolean reminderSent;
}