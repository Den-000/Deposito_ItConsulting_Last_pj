package com.example.demo.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

/**
 * SERVIZIO per la generazione dei QR CODE.
 */
@Service
public class QRCodeService {

    /**
     * Genera un identificatore univoco per il QR code.
     */
    public String generate() {
        return UUID.randomUUID().toString();
    }
}