package com.example.demo.dto;

import lombok.Data;

/**
 * DTO (Data Transfer Object) per il CHECK-IN del biglietto.
 * 
 * Serve a trasferire SOLO i dati necessari tra client e server.
 */
@Data
public class CheckInRequest {

    /**
     * QR code del biglietto da validare.
     */
    private String qrCode;
}