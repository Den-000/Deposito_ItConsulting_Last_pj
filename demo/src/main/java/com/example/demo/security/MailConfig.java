package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * CONFIGURAZIONE EMAIL.
 * 
 * Definisce il bean necessario per inviare email nel sistema.
 */
@Configuration
public class MailConfig {

    /**
     * Crea e registra il mail sender nello Spring Context.
     * Attualmente configurazione base.
     */
    @Bean
    public JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}